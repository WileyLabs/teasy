package com.wiley.autotest.utils;

import com.wiley.autotest.selenium.SeleniumHolder;

/**
 * User: ntyukavkin
 * Date: 11.10.14
 * Time: 15:30
 */
public final class ExecutionUtils {

    public static boolean isIE() {
        return SeleniumHolder.getDriverName().toLowerCase().contains("ie");
    }

    public static  boolean isFF() {
        return SeleniumHolder.getDriverName().equalsIgnoreCase("firefox");
    }

    public static  boolean isChrome() {
        return SeleniumHolder.getDriverName().equalsIgnoreCase("chrome");
    }

    public static  boolean isSafari() {
        return SeleniumHolder.getDriverName().equalsIgnoreCase("safari");
    }

    public static  boolean isWindows() {
        return SeleniumHolder.getPlatform().equalsIgnoreCase("windows");
    }

    public static  boolean isAndroid() {
        return SeleniumHolder.getPlatform().equalsIgnoreCase("android");
    }

    public static  boolean isIOs() {
        return SeleniumHolder.getPlatform().equalsIgnoreCase("ios");
    }

    public static  boolean isMac() {
        return SeleniumHolder.getPlatform().equalsIgnoreCase("mac");
    }
}
