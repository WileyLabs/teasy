package com.wiley.autotest.selenium;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.SessionId;

import java.util.ArrayList;
import java.util.List;

/**
 * @author alexey.a.semenov
 */
public final class SeleniumHolder {

    private static String browser;
    private static final ThreadLocal<WebDriver> driverHolder = new ThreadLocal<>();
    private static final ThreadLocal<Integer> timeoutHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> driverName = ThreadLocal.withInitial(() -> "");
    private static final ThreadLocal<String> parameterBrowserName = ThreadLocal.withInitial(() -> "browser");
    private static final ThreadLocal<String> parameterPlatformName = ThreadLocal.withInitial(() -> "platform");
    private static final ThreadLocal<String> platform = ThreadLocal.withInitial(() -> "platform");
    private static final ThreadLocal<String> mainWindowHandle = new ThreadLocal<>();
    private static final ThreadLocal<String> nodeIp = ThreadLocal.withInitial(() -> "localhost");
    private static final ThreadLocal<SessionId> sessionId = new ThreadLocal<>();
    private static String customElementFactoryClass;
    private static final ThreadLocal<AppiumDriver> appiumDriver = new ThreadLocal<>();
    private static final ThreadLocal<IOSDriver> iosDriverHolder = new ThreadLocal<>();
    private static final ThreadLocal<AndroidDriver> androidDriverHolder = new ThreadLocal<>();
    private static final ThreadLocal<List<String>> activeProfilesList = ThreadLocal.withInitial(ArrayList::new);
    private static final ThreadLocal<String> platformName = new ThreadLocal<String>();

    private SeleniumHolder() {
    }

    public static WebDriver getWebDriver() {
        return driverHolder.get();
    }

    public static void setWebDriver(final WebDriver value) {
        driverHolder.set(value);
    }

    public static Integer getTimeoutInSeconds() {
        return timeoutHolder.get();
    }

    public static void setTimeoutInSeconds(final Integer timeoutInSeconds) {
        timeoutHolder.set(timeoutInSeconds);
    }

    public static String getDriverName() {
        return driverName.get();
    }

    public static void setDriverName(String value) {
        setBrowser(value);
        driverName.set(value);
    }

    public static String getBrowser() {
        return browser;
    }

    public static void setBrowser(String browser) {
        SeleniumHolder.browser = browser;
    }

    public static String getPlatform() {
        return platform.get();
    }

    public static void setPlatform(String value) {
        platform.set(value);
    }

    public static String getParameterBrowserName() {
        return parameterBrowserName.get();
    }

    public static void setParameterBrowserName(String browserName) {
        parameterBrowserName.set(browserName);
    }

    public static String getParameterPlatformName() {
        return parameterPlatformName.get();
    }

    public static void setParameterPlatformName(String platform) {
        parameterPlatformName.set(platform);
    }

    public static SessionId getSessionId() {
        return sessionId.get();
    }

    public static void setSessionId(SessionId sessionId) {
        SeleniumHolder.sessionId.set(sessionId);
    }

    public static String getNodeIp() {
        return nodeIp.get();
    }

    public static void setNodeIp(String nodeIp) {
        SeleniumHolder.nodeIp.set(nodeIp);
    }

    public static void setMainWindowHandle(String windowHandle) {
        mainWindowHandle.set(windowHandle);
    }

    public static String getMainWindowHandle() {
        return mainWindowHandle.get();
    }

    public static String getCustomElementFactoryClass() {
        return customElementFactoryClass;
    }

    public static void setCustomElementFactoryClass(String customElementFactoryClass) {
        SeleniumHolder.customElementFactoryClass = customElementFactoryClass;
    }

    public static AppiumDriver getAppiumDriver() {
        return appiumDriver.get();
    }

    public static void setAppiumDriver(final AppiumDriver appiumDriver) {
        SeleniumHolder.appiumDriver.set(appiumDriver);
    }

    public static IOSDriver getIOSDriver() {
        return iosDriverHolder.get();
    }

    public static void setIOSDriver(final IOSDriver iosDriver) {
        iosDriverHolder.set(iosDriver);
    }

    public static AndroidDriver getAndroidDriver() {
        return androidDriverHolder.get();
    }

    public static void setAndroidDriver(final AndroidDriver androidDriver) {
        androidDriverHolder.set(androidDriver);
    }

    public static void setActiveProfilesList(List<String> profilesList) {
        activeProfilesList.set(profilesList);
    }

    public static List<String> getActiveProfilesList() {
        return activeProfilesList.get();
    }

    public static String getPlatformName() {
        return platformName.get();
    }

    public static void setPlatformName(String value) {
        platformName.set(value);
    }
}
