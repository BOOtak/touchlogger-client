package org.leyfer.thesis.TouchLogger.helper;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class GestureBuffer {
    private JSONArray mGesturesArray;
    private int mBufferSize;

    public GestureBuffer() {
        mGesturesArray = new JSONArray();
        mBufferSize = 0;
    }

    public GestureBuffer(GestureBuffer buffer) {
        this.mGesturesArray = new JSONArray();
        try {
            for (int i = 0; i < buffer.getGestures().length(); i++) {
                mGesturesArray.put(buffer.getGestures().get(i));
            }

            this.mBufferSize = buffer.getBufferSize();
        } catch (JSONException e) {
            this.mGesturesArray = new JSONArray();
            this.mBufferSize = 0;
        }
    }

    public void addGesture(JSONObject gesture) {
        mGesturesArray.put(gesture);
        mBufferSize += gesture.toString().length();
    }

    public JSONArray getGestures() {
        return mGesturesArray;
    }

    public int getBufferSize() {
        return mBufferSize;
    }

    public void clear() {
        mBufferSize = 0;
        mGesturesArray = new JSONArray();
    }

    @Override
    public String toString() {
        return mGesturesArray.toString();
    }
}
