package org.leyfer.thesis.TouchLogger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.leyfer.thesis.TouchLogger.service.TouchReaderService;

import java.io.*;

public class MainActivity extends Activity {

    private boolean isRunning = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    Intent touchLoggerIntent = new Intent(getApplicationContext(), TouchReaderService.class);
                    touchLoggerIntent.setAction(TouchReaderService.ACTION_STOP);
                    startService(touchLoggerIntent);
                    button.setText(R.string.start_service);
                    isRunning = false;
                } else {
                    Intent touchLoggerIntent = new Intent(getApplicationContext(), TouchReaderService.class);
                    touchLoggerIntent.setAction(TouchReaderService.ACTION_START);
                    startService(touchLoggerIntent);
                    button.setText(R.string.stop_service);
                    isRunning = true;
                }
            }
        });
    }
}
