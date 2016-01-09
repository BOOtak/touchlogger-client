package org.leyfer.thesis.TouchLogger;

import android.content.Context;

import org.json.JSONObject;
import org.leyfer.thesis.TouchLogger.helper.GestureBuffer;
import org.leyfer.thesis.TouchLogger.task.SaveGesturesTask;

public class TouchSaver {
    private static TouchSaver ourInstance;
    private final int maxBufferSize = 2 << 17;
    private final Context mContext;

    private GestureBuffer gestureBuffer = new GestureBuffer();

    private TouchSaver(Context mContext) {
        this.mContext = mContext;
    }

    public static TouchSaver getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new TouchSaver(context);
        }
        return ourInstance;
    }

    public void saveGesture(JSONObject gesture) {
        gestureBuffer.addGesture(gesture);
        if (gestureBuffer.getBufferSize() >= maxBufferSize) {
            new SaveGesturesTask(mContext).doInBackground(new GestureBuffer(gestureBuffer));
            gestureBuffer.clear();
        }
    }
}
