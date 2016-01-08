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

    // LG Nexus 5 (AOSP)
    public static final TouchConfig hammerhead_AOSP_on_HammerHead_Config = new TouchConfig(
            "/dev/input/event1",
            new Code("0003", "0000"),
            new Range(0, 9, 255),
            new Type("002f", "0035", "0036", "003a", "0039")
    );

    // Samsung galaxy note 3
    public static final TouchConfig MSM8974_SM_N9005_Config = new TouchConfig(
            "/dev/input/event1",
            new Code("0003", "0000"),
            new Range(0, 9, 255),
            new Type("002f", "0035", "0036", "003b", "0039")
    );

    // LG G2
    public static final TouchConfig MSM8974_LG_D802_Config = new TouchConfig(
            "/dev/input/event1",
            new Code("0003", "0000"),
            new Range(0, 9, 255),
            new Type("002f", "0035", "0036", "003a", "0039")
    );

    public static final Map<String, TouchConfig> configMap = new HashMap<String, TouchConfig>() {{
        put("hammerhead,Nexus 5", hammerhead_Nexus_5_Config);
        put("hammerhead,AOSP on HammerHead", hammerhead_AOSP_on_HammerHead_Config);
        put("MSM8974,SM-N9005", MSM8974_SM_N9005_Config);
        put("MSM8974,LG-D802", MSM8974_LG_D802_Config);
    }};
}
