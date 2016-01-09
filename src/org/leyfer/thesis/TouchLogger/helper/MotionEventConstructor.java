package org.leyfer.thesis.TouchLogger.helper;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.leyfer.thesis.TouchLogger.config.TouchConfig;

public class MotionEventConstructor {
    private JSONObject motionEvent;
    private TouchConfig config;
    private boolean motionEventComplete;

    private String eventType;

    private class Slot {
        public int id;
        public int currentX;
        public int currentY;
        public float currentPressure;
        public boolean shouldDelete;
    }

    private final Slot[] slots;
    private int currentSlot;

    public MotionEventConstructor(TouchConfig config) {
        this.config = config;
        slots = new Slot[config.RANGE.SLOT_MAX - config.RANGE.SLOT_MIN + 1];
        currentSlot = 0;
        motionEvent = new JSONObject();
        motionEventComplete = false;
    }

    public boolean isMotionEventReady() {
        return motionEventComplete;
    }

    public JSONObject getMotionEvent() throws JSONException {
        if (isMotionEventReady()) {
            if (motionEvent.optString("prefix").isEmpty()) {
                motionEvent.put("prefix", "MOVE");
            }

            int i;
            JSONArray pointers = new JSONArray();
            for (i = 0; i < slots.length; i++) {
                if (slots[i] != null) {
                    pointers.put(
                            new JSONObject()
                                    .put("index", getPointerIndex(i))
                                    .put("id", i)
                                    .put("x", slots[i].currentX)
                                    .put("y", slots[i].currentY)
                                    .put("pressure", slots[i].currentPressure)
                    );
                }
            }

            for (i = 0; i < slots.length; i++) {
                if ((slots[i] != null) && (slots[i].shouldDelete)) {
                    slots[i] = null;
                }
            }

            motionEvent.put("pointers", pointers);
            return motionEvent;
        } else {
            return null;
        }
    }

    private int getPointerCount() {
        int count = 0;
        for (Slot slot: slots) {
            if (slot != null) {
                count++;
            }
        }
        return count;
    }

    private int getPointerIndex(int id) {
        int count = 0;
        int i;
        for (i = 0; i < slots.length; i++) {
            if (slots[i] != null) {
                if (i == id) {
                    return count;
                } else {
                    count++;
                }
            }
        }
        return count;
    }

    public void update(String rawTouchEvent) {
        // Example: "[   57287.727631] 0003 0035 000001b3"
        if (motionEventComplete) {
            motionEventComplete = false;
            motionEvent = new JSONObject();
        }

        String[] rawParts = rawTouchEvent.split("] ");
        String[] rawTimestampParts = (rawParts[0].split("\\["))[1].split(" ");
        String[] timestampParts = rawTimestampParts[rawTimestampParts.length - 1].split("\\.");
        Long timestamp = Long.parseLong(timestampParts[0]) * 1000 + Long.parseLong(timestampParts[1]) / 1000;

        String[] touchEventParts = rawParts[1].split(" ");
        try {
            if (motionEvent.optLong("timestamp", 0) == 0) {
                motionEvent.put("timestamp", timestamp);
            }
            String code = touchEventParts[0];
            if (code.equals(config.CODE.DELIMITER)) {
                motionEventComplete = true;
            } else if (code.equals(config.CODE.ABS)) {
                String type = touchEventParts[1];
                if (type.equals(config.TYPE.TRACKING_ID)) {
                    if (touchEventParts[2].equals("ffffffff")) {
                        if (getPointerCount() == 1) {
                            motionEvent.put("prefix", "UP");
                        } else {
                            motionEvent.put("prefix", "POINTER_UP");
                            motionEvent.put("action_pointer_index", getPointerIndex(currentSlot));
                        }
                        slots[currentSlot].shouldDelete = true;
                    } else {
                        if (getPointerCount() == 0) {
                            motionEvent.put("prefix", "DOWN");
                            slots[currentSlot] = new Slot();
                            slots[currentSlot].id = Integer.parseInt(touchEventParts[2], 16);
                        } else {
                            slots[currentSlot] = new Slot();
                            slots[currentSlot].id = Integer.parseInt(touchEventParts[2], 16);
                            motionEvent.put("prefix", "POINTER_DOWN");
                            motionEvent.put("action_pointer_index", getPointerIndex(currentSlot));
                        }
                    }
                } else if (type.equals(config.TYPE.X)) {
                    slots[currentSlot].currentX = Integer.parseInt(touchEventParts[2], 16);
                } else if (type.equals(config.TYPE.Y)) {
                    slots[currentSlot].currentY = Integer.parseInt(touchEventParts[2], 16);
                } else if (type.equals(config.TYPE.PRESSURE)) {
                    slots[currentSlot].currentPressure =
                            ((float)Integer.parseInt(touchEventParts[2], 16)) / config.RANGE.PRESSURE_MAX;
                } else if (type.equals(config.TYPE.SLOT)) {
                    currentSlot = Integer.parseInt(touchEventParts[2], 16);
                } else {
                    // skip other types of touch events
                }
            }
        } catch (Exception e) {
            Log.e("TestLogger", e.getMessage());
            throw new RuntimeException("Bad parser");
        }
    }
}
