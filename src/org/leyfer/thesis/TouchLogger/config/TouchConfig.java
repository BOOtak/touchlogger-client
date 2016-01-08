package org.leyfer.thesis.TouchLogger.config;

public class TouchConfig {
    public final String INPUT_DEVICE_PATH;
    public final Code CODE;
    public final Range RANGE;
    public final Type TYPE;

    public TouchConfig(String inputDevicePath, int slotMin, int slotMax, String abs, String delimiter, String slot, String x, String y, String pressure, int pressureMax, String trackingId) {
        this.INPUT_DEVICE_PATH = inputDevicePath;
        this.CODE = new Code(abs, delimiter);
        this.RANGE = new Range(slotMin, slotMax, pressureMax);
        this.TYPE = new Type(slot, x, y, pressure, trackingId);
    }

    public TouchConfig(String inputDevicePath, Code code, Range range, Type type) {
        this.INPUT_DEVICE_PATH = inputDevicePath;
        this.CODE = code;
        this.RANGE = range;
        this.TYPE = type;
    }
}
