package com.wiley.holders;

import org.openqa.selenium.remote.SessionId;

/**
 * User: ntyukavkin
 * Date: 12.04.2018
 * Time: 15:13
 */
public class TestParamsHolder {

    private static ThreadLocal<String> testName = new ThreadLocal<>();
    private static ThreadLocal<String> platform = new ThreadLocal<>();
    private static ThreadLocal<String> browser = new ThreadLocal<>();
    private static ThreadLocal<String> nodeIP = new ThreadLocal<>();
    private static ThreadLocal<Integer> chromePort = new ThreadLocal<>();
    private static ThreadLocal<SessionId> sessionId = new ThreadLocal<>();

    public static String getTestName() {
        return testName.get();
    }

    public static void setTestName(String testName) {
        TestParamsHolder.testName.set(testName);
    }

    public static String getPlatform() {
        return platform.get();
    }

    public static void setPlatform(String platform) {
        TestParamsHolder.platform.set(platform);
    }

    public static String getBrowser() {
        return browser.get();
    }

    public static void setBrowser(String browser) {
        TestParamsHolder.browser.set(browser);
    }

    public static String getNodeIP() {
        return nodeIP.get();
    }

    public static void setNodeIP(String nodeIP) {
        TestParamsHolder.nodeIP.set(nodeIP);
    }

    public static Integer getChromePort() {
        return chromePort.get();
    }

    public static void setChromePort(Integer chromePort) {
        TestParamsHolder.chromePort.set(chromePort);
    }

    public static SessionId getSessionId() {
        return sessionId.get();
    }

    public static void setSessionId(SessionId sessionId) {
        TestParamsHolder.sessionId.set(sessionId);
    }
}
