package org.leyfer.thesis.TouchLogger.config;

public class Range {
    public final int SLOT_MIN;
    public final int SLOT_MAX;
    public final int PRESSURE_MAX;

    public Range(int slotMin, int slotMax, int pressureMax) {
        SLOT_MIN = slotMin;
        SLOT_MAX = slotMax;
        PRESSURE_MAX = pressureMax;
    }
}