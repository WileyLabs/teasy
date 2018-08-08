package com.wiley.driver.factory;

import org.openqa.selenium.WebDriver;

/**
 * Abstract factory to provide instance of WebDriver
 */
public interface DriverFactory {

    String FIREFOX = "firefox";
    String GECKO = "gecko";
    String CHROME = "chrome";
    String SAFARI = "safari";
    //constant to be set as a Driver Name
    String SAFARI_TECH_PREVIEW_DIVER_NAME = "safariTechnologyPreview";
    //constant for comparison with passed driver name. (lower-cased as we compare it with a lower-cased value)
    String SAFARI_TECH_PREVIEW = "safaritechnologypreview";
    String IE = "ie";
    String EDGE = "edge";
    String IE11 = "ie11";
    String IE10 = "ie10";
    String NATIVE_APP = "native_app";
    String EMPTY = "";


    String IOS = "ios";
    String ANDROID = "android";
    String WINDOWS = "windows";
    String LINUX = "linux";
    String MAC = "mac";

    /* version of IE Driver that works */
    String STABLE_IE_DRIVER_VERSION = "3.4.0";

    WebDriver get();
}
