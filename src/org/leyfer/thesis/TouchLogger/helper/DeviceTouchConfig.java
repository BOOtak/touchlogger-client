package org.leyfer.thesis.TouchLogger.helper;

import org.leyfer.thesis.TouchLogger.config.Code;
import org.leyfer.thesis.TouchLogger.config.Range;
import org.leyfer.thesis.TouchLogger.config.TouchConfig;
import org.leyfer.thesis.TouchLogger.config.Type;

import java.util.HashMap;
import java.util.Map;

public class DeviceTouchConfig {
    // LG Nexus 5 (stock)
    public static final TouchConfig hammerhead_Nexus_5_Config = new TouchConfig(
            "/dev/input/event1",
            new Code("0003", "0000"),
            new Range(0, 9, 255),
            new Type("002f", "0035", "0036", "003a", "0039")
    );

    // Samsung galaxy note 3
    public static final TouchConfig MSM8974_SM_N900_Config = new TouchConfig(
            "/dev/input/event1",
            new Code("0003", "0000"),
            new Range(0, 9, 255),
            new Type("002f", "0035", "0036", "0032", "0039")
    );

    // LG G2
    public static final TouchConfig MSM8974_LG_D802_Config = new TouchConfig(
            "/dev/input/event1",
            new Code("0003", "0000"),
            new Range(0, 9, 255),
            new Type("002f", "0035", "0036", "003a", "0039")
    );

    // Lenovo K900
    public static final TouchConfig clovertrail_Lenovo_K900_ROW_Config = new TouchConfig(
            "/dev/input/event0",
            new Code("0003", "0000"),
            new Range(0, 9, 255),
            new Type("002f", "0035", "0036", "003b", "0039")
    );

    // Galaxy S4 mini
    public static final TouchConfig MSM8960_GT_I9190_Config = new TouchConfig(
            "/dev/input/event2",
            new Code("0003", "0000"),
            new Range(0, 7, 255),
            new Type("002f", "0035", "0036", "003a", "0039")
    );

    // Samsung Galaxy Grand 2
    public static final TouchConfig MSM8226_SM_G7102_Config = new TouchConfig(
            "/dev/input/event1",
            new Code("0003", "0000"),
            new Range(0, 4, 255),
            new Type("002f", "0035", "0036", "0032", "0039")
    );

    // Samsung Galaxy Grand Duos
    public static final TouchConfig CAPRI_GT_I9082_Config = new TouchConfig(
            "/dev/input/event4",
            new Code("0003", "0000"),
            new Range(0, 31, 255),
            new Type("002f", "0035", "0036", "003a", "0039")
    );

    // Htc desire C
    public static final TouchConfig GOLFU_HTC_DESIRE_C_Config = new TouchConfig(
            "/dev/input/event5",
            new Code("0003", "0000"),
            new Range(0, 3, 30),
            new Type("002f", "0035", "0036", "0032", "0039")
    );

    // Xperia S
    public static final TouchConfig fuji_Xperia_S_Config = new TouchConfig(
            "/dev/input/event5",
            new Code("0003", "0000"),
            new Range(0, 9, 255),
            new Type("002f", "0035", "0036", "003a", "0039")
    );

    // Galaxy note edge
    public static final TouchConfig APQ8084_SM_N915F_Config = new TouchConfig(
            "/dev/input/event1",
            new Code("0003", "0000"),
            new Range(0, 9, 255),
            new Type("002f", "0035", "0036", "003b", "0039")
    );

    // Sony xperia SP cm12
    public static final TouchConfig MSM8960_Xperia_SP_Config = new TouchConfig(
            "/dev/input/event7",
            new Code("0003", "0000"),
            new Range(0, 10, 255),
            new Type("002f", "0035", "0036", "003a", "0039")
    );

    public static final Map<String, TouchConfig> configMap = new HashMap<String, TouchConfig>() {{
        put("HAMMERHEAD,NEXUS 5", hammerhead_Nexus_5_Config);
        put("HAMMERHEAD,NEXUS 5 CAF", hammerhead_Nexus_5_Config);
        put("CAPRI,GT-I9082", CAPRI_GT_I9082_Config);
        put("MSM8226,SM-G7102", MSM8226_SM_G7102_Config);
        put("HAMMERHEAD,AOSP ON HAMMERHEAD", hammerhead_Nexus_5_Config);
        put("UNIVERSAL5420,SM-N900", MSM8974_SM_N900_Config);
        put("MSM8974,SM-N9005", MSM8974_SM_N900_Config);
        put("GALBI,LG-D802", MSM8974_LG_D802_Config);
        put("CLOVERTRAIL,LENOVO K900_ROW", clovertrail_Lenovo_K900_ROW_Config);
        put("MSM8960,GT-I9190", MSM8960_GT_I9190_Config);
        put("GOLFU,HTC DESIRE C", GOLFU_HTC_DESIRE_C_Config);
        put("FUJI,XPERIA S", fuji_Xperia_S_Config);
        put("APQ8084,SM-N915F", APQ8084_SM_N915F_Config);
        put("MSM8960,XPERIA SP", MSM8960_Xperia_SP_Config);
    }};
}
