package org.leyfer.thesis.TouchLogger.config;

public class Type {
    public final String SLOT;
    public final String X;
    public final String Y;
    public final String PRESSURE;
    public final String TRACKING_ID;

    public Type (String slot, String x, String y, String pressure, String trackingId) {
        SLOT = slot;
        X = x;
        Y = y;
        PRESSURE = pressure;
        TRACKING_ID = trackingId;
    }
}
