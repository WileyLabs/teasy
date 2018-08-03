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
    String SAFARI_TECHNOLOGY_PREVIEW = "safariTechnologyPreview";
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
