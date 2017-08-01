package com.wiley.autotest.utils;

import com.wiley.autotest.selenium.SeleniumHolder;

/**
 * User: ntyukavkin
 * Date: 11.10.14
 * Time: 15:30
 */
public final class ExecutionUtils {

    public static boolean isIE() {
        return getDriverName().startsWith("ie");
    }

    public static boolean isFF() {
        return getDriverName().equals("firefox");
    }

    public static boolean isChrome() {
        return getDriverName().equals("chrome");
    }

    public static boolean isSafari() {
        return getDriverName().startsWith("safari") && isMac();
    }

    public static boolean isMobileSafari() {
        return getDriverName().equals("safari") && isIOs();
    }

    public static boolean isWindows() {
        return getPlatformName().equals("windows");
    }

    public static boolean isMac() {
        return getPlatformName().equals("mac");
    }

    public static boolean isAndroid() {
        return getPlatformName().equals("android");
    }

    public static boolean isIOs() {
        return getPlatformName().equals("ios");
    }

    private static String getDriverName() {
        return SeleniumHolder.getDriverName().toLowerCase();
    }

    private static String getPlatformName() {
        return SeleniumHolder.getPlatform().toLowerCase();
    }
}
