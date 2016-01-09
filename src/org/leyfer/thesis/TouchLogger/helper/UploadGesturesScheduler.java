package org.leyfer.thesis.TouchLogger.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class UploadGesturesScheduler {
    private final String GESTURE_SHAREDPREFS_ID = "org.leyfer.thesis.sharedPrefsKey";
    private final String NEED_UPLOAD = "org.leyfer.thesis.needUpload";
    private final Context mContext;

    public boolean needUploadGestures() {
        SharedPreferences prefs = mContext.getSharedPreferences(GESTURE_SHAREDPREFS_ID, Context.MODE_PRIVATE);
        return prefs.getBoolean(NEED_UPLOAD, false);
    }

    public void setNeedUploadGestures(boolean state) {
        SharedPreferences prefs = mContext.getSharedPreferences(GESTURE_SHAREDPREFS_ID, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(NEED_UPLOAD, state).apply();
    }

    private static UploadGesturesScheduler ourInstance;

    public static UploadGesturesScheduler getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new UploadGesturesScheduler(context);
        }
        return ourInstance;
    }

    private UploadGesturesScheduler(Context context) {
        mContext = context;
    }
}
