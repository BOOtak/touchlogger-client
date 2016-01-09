package org.leyfer.thesis.TouchLogger.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import org.json.JSONException;
import org.leyfer.thesis.TouchLogger.MainActivity;
import org.leyfer.thesis.TouchLogger.helper.DeviceTouchConfig;
import org.leyfer.thesis.TouchLogger.helper.MotionEventConstructor;
import org.leyfer.thesis.TouchLogger.config.TouchConfig;
import org.leyfer.thesis.TouchLogger.notification.WatchNotification;
import org.leyfer.thesis.TouchLogger.notification.helper.NotificationAction;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class TouchReaderService extends IntentService {

    public static final String ACTION_START = "org.leyfer.thesis.ACTION_START";
    public static final String ACTION_PAUSE = "org.leyfer.thesis.ACTION_PAUSE";
    public static final String ACTION_RESUME = "org.leyfer.thesis.ACTION_RESUME";
    public static final String ACTION_STOP = "org.leyfer.thesis.ACTION_STOP";

    private final int BUFFER_LENGTH = 32;
    private WatchNotification mNotification;
    private boolean mActive = true;
    private NotificationManager mNotificationManager;
    private boolean isRunning = true;

    public TouchReaderService() {
        super("TouchReaderService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotification = new WatchNotification(getApplicationContext());
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        readAndParseTouches();
    }

    private void killGetevent() {
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            String command="pkill getevent";
            os.writeBytes(command + "\n");
            os.close();
        } catch (IOException e) {
            Log.e("TestLogger", e.getMessage());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        String action = intent.getAction();
        if (ACTION_PAUSE.equals(action)) {
            mActive = false;
            startForeground(WatchNotification.NOTIFICATION_ID,
                    mNotification.getNotification(NotificationAction.ACTION_PAUSED));
        } else if (ACTION_RESUME.equals(action)) {
            mActive = true;
            startForeground(WatchNotification.NOTIFICATION_ID,
                    mNotification.getNotification(NotificationAction.ACTION_INPROGRESS));
        } else if (ACTION_START.equals(action)) {
            mActive = true;
            startForeground(WatchNotification.NOTIFICATION_ID,
                    mNotification.getNotification(NotificationAction.ACTION_INPROGRESS));
            sendBroadcast(
                    new Intent(MainActivity.ACTION_SERVICE_STATE)
                            .putExtra(MainActivity.SERVICE_ISRUNNING_EXTRA, true)
            );
        } else if (ACTION_STOP.equals(action)) {
            mActive = false;
            isRunning = false;
            killGetevent();
            mNotificationManager.cancel(WatchNotification.NOTIFICATION_ID);
            sendBroadcast(
                    new Intent(MainActivity.ACTION_SERVICE_STATE)
                            .putExtra(MainActivity.SERVICE_ISRUNNING_EXTRA, false)
            );
        }

        return START_NOT_STICKY;
    }

    private void readAndParseTouches() {
        MotionEventConstructor eventConstructor;

        TouchConfig config = DeviceTouchConfig.configMap.get(String.format("%s,%s", Build.BOARD, Build.MODEL));
        if (config == null) {
            throw new RuntimeException("bad device");
        }

        eventConstructor = new MotionEventConstructor(config);
        try {
            Process process = Runtime.getRuntime().exec("su");

            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            String command=String.format("getevent -t %s", config.INPUT_DEVICE_PATH);
            os.writeBytes(command + "\n");
            os.close();

            InputStream is = new BufferedInputStream(process.getInputStream());
            StringBuilder result = new StringBuilder();
            byte[] buffer = new byte[BUFFER_LENGTH];
            while(isRunning && !Thread.currentThread().isInterrupted()) {
                int availableBytes = is.available();
                if (availableBytes > 0) {
                    int readed = is.read(buffer);
                    if (readed > 0) {
                        result.append(new String(buffer, 0, readed));
                    }
                }
                else {
                    try {
                        Thread.sleep(500);
                    }
                    catch (InterruptedException e)
                    {
                        isRunning = false;
                    }
                }

                String logData = result.toString();
                String[] strings = logData.split("\n");

                boolean isStringcompleted = logData.endsWith("\n");
                result = new StringBuilder();
                if (!isStringcompleted) {
                    result.append(strings[strings.length - 1]);
                }

                int i;
                int length;
                if (isStringcompleted) {
                    length = strings.length;
                } else {
                    length = strings.length - 1;
                }
                for (i = 0; i < length; i++) {
                    parseRawTouchEvent(eventConstructor, strings[i]);
                }
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("TestLogger", e.getMessage());
        }
    }

    private void parseRawTouchEvent(MotionEventConstructor constructor, String rawTouchEvent) throws JSONException {
        constructor.update(rawTouchEvent);
        if (constructor.isMotionEventReady()) {
            if (mActive) {
                Log.d("TestLogger", constructor.getMotionEvent().toString());
            }
        }
    }
}
