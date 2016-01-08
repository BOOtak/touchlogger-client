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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent touchLoggerIntent = new Intent(getApplicationContext(), TouchReaderService.class);
                startService(touchLoggerIntent);
            }
        });
    }
}
