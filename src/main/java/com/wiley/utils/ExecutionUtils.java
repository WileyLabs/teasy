package com.wiley.utils;

import com.wiley.config.Configuration;

/**
 * User: ntyukavkin
 * Date: 13.04.2018
 * Time: 11:59
 */
public class ExecutionUtils {

    public static boolean isFF() {
        return Configuration.browser.equals("firefox");
    }

    public static boolean isChrome() {
        return Configuration.browser.equals("chrome");
    }
}
