package com.wiley.config;

import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Configuration for a test run
 */
public final class Configuration {

    private Configuration() {
    }

    public static String platform = System.getProperty("teasy.platform", "windows");

    public static String browser = System.getProperty("teasy.browser", "chrome");

    public static int timeout = Integer.parseInt(System.getProperty("teasy.timeout", "10"));

    public static boolean runWithGrid = Boolean.parseBoolean(System.getProperty("teasy.runWithGrid", "false"));

    public static String gridHubUrl = System.getProperty("teasy.gridHubUrl", "http://localhost:4444/wd/hub");

    public static boolean headless = Boolean.parseBoolean(System.getProperty("teasy.headless", "false"));

    public static int restartCount = Integer.parseInt(System.getProperty("teasy.restart.count", "5"));

    public static int tryToStartDriverCount = Integer.parseInt(System.getProperty("teasy.start.count", "3"));

    //null means that default element factory will be used
    public static String elementFactoryClass = System.getProperty("teasy.elementFactoryClass");

    public static DesiredCapabilities customCaps = new DesiredCapabilities();
}
