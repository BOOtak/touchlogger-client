package org.leyfer.thesis.TouchLogger.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import org.json.JSONException;
import org.leyfer.thesis.TouchLogger.helper.DeviceTouchConfig;
import org.leyfer.thesis.TouchLogger.helper.MotionEventConstructor;
import org.leyfer.thesis.TouchLogger.config.TouchConfig;
import org.leyfer.thesis.TouchLogger.notification.WatchNotification;
import org.leyfer.thesis.TouchLogger.notification.helper.NotificationActionEnum;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class TouchReaderService extends IntentService {

    private final int BUFFER_LENGTH = 32;
    private WatchNotification mNotification;
    private MotionEventConstructor eventConstructor;
    private TouchConfig mConfig;

    public TouchReaderService() {
        super("TouchReaderService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotification = new WatchNotification(getApplicationContext(), NotificationActionEnum.ACTION_INPROGRESS);
        mConfig = DeviceTouchConfig.configMap.get(String.format("%s,%s", Build.BOARD, Build.MODEL));
        if (mConfig == null) {
            throw new RuntimeException("bad device");
        }
        eventConstructor = new MotionEventConstructor(mConfig);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            Process process = Runtime.getRuntime().exec("su");

            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            String command=String.format("getevent -t %s", mConfig.INPUT_DEVICE_PATH);
            os.writeBytes(command + "\n");
            os.close();

            InputStream is = new BufferedInputStream(process.getInputStream());
            StringBuilder result = new StringBuilder();
            byte[] buffer = new byte[BUFFER_LENGTH];
            boolean isRunning = true;
            while(isRunning) {
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
                    parseRawTouchEvent(strings[i]);
                }
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("TestLogger", e.getMessage());
        }
    }

    private void parseRawTouchEvent(String rawTouchEvent) throws JSONException {
        eventConstructor.update(rawTouchEvent);
        if (eventConstructor.isMotionEventReady()) {
            Log.d("TestLogger", eventConstructor.getMotionEvent().toString());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        startForeground(WatchNotification.NOTIFICATION_ID, mNotification.getNotificatoin());
        return START_NOT_STICKY;
    }
}
