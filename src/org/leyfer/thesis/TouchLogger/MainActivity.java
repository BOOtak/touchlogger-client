package org.leyfer.thesis.TouchLogger;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.*;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import org.leyfer.thesis.TouchLogger.receiver.UploadReceiver;
import org.leyfer.thesis.TouchLogger.service.TouchReaderService;

public class MainActivity extends Activity {

    public static final String ACTION_SERVICE_STATE = "org.leyfer.thesis.servicestate";
    public static final String SERVICE_ISRUNNING_EXTRA = "org.leyfer.svcrunning.xtra";

    private final String SHARED_PREFS_NAME = "org.leyfer.thesis.shprefs";
    private final String SERVICE_ISRUNNING_KEY = "org.leyfer.svcrunning.key";
    private Button mButton;
    private Intent mIntent;
    private SharedPreferences preferences;
    private boolean registered = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mButton = (Button) findViewById(R.id.button);
        preferences = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
        if (preferences.contains(SERVICE_ISRUNNING_KEY)) {
            if (preferences.getBoolean(SERVICE_ISRUNNING_KEY, false)) {
                setStop();
            } else {
                setStart();
            }
        } else {
            setStart();
        }

        IntentFilter intentFilter = new IntentFilter(ACTION_SERVICE_STATE);
        registerReceiver(touchServiceStateReceiver, intentFilter);
        registered = true;

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(mIntent);
            }
        });
        setAlarm();
    }

    @Override
    protected void onStop() {
        if (registered) {
            unregisterReceiver(touchServiceStateReceiver);
            registered = false;
        }
        super.onStop();
    }


    private void setAlarm() {
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent uploadGesturesIntent = new Intent(UploadReceiver.UPLOAD_GESTURES);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, uploadGesturesIntent, 0);

        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                30 * 60 * 1000,
                30 * 60 * 1000,
                pendingIntent);

        Log.d("TouchLogger", "Start alarm clock");
    }

    private void setStop() {
        mButton.setText(R.string.stop_service);
        mIntent = new Intent(getApplicationContext(), TouchReaderService.class);
        mIntent.setAction(TouchReaderService.ACTION_STOP);
    }

    private void setStart() {
        mButton.setText(R.string.start_service);
        mIntent = new Intent(getApplicationContext(), TouchReaderService.class);
        mIntent.setAction(TouchReaderService.ACTION_START);
    }

    private BroadcastReceiver touchServiceStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(SERVICE_ISRUNNING_EXTRA)) {
                preferences.edit().putBoolean(SERVICE_ISRUNNING_KEY,
                        intent.getBooleanExtra(SERVICE_ISRUNNING_EXTRA, false)).apply();
                if (intent.getBooleanExtra(SERVICE_ISRUNNING_EXTRA, false)) {
                    setStop();
                } else {
                    setStart();
                }
            } else {
                setStart();
            }
        }
    };
}
