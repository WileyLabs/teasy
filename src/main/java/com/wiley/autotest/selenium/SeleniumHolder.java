package com.wiley.autotest.selenium;

import net.lightbody.bmp.proxy.ProxyServer;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.SessionId;

/**
 * @author alexey.a.semenov
 */
public final class SeleniumHolder {

    /**
     * private constructor for utils class
     */
    private SeleniumHolder() {
    }

    private static String browser;
    private static final ThreadLocal<WebDriver> driverHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> driverName = new ThreadLocal<>();
    private static final ThreadLocal<String> parameterBrowserName = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return "browser";
        }
    };
    private static final ThreadLocal<String> parameterPlatformName = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return "platform";
        }
    };
    private static final ThreadLocal<ProxyServer> proxyServer = new ThreadLocal<>();
    private static final ThreadLocal<Proxy> proxy = new ThreadLocal<>();
    private static final ThreadLocal<String> platform = new ThreadLocal<>();
    private static final ThreadLocal<String> mainWindowHandle = new ThreadLocal<>();
    private static final ThreadLocal<String> nodeIp = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return "localhost";
        }
    };
    private static final ThreadLocal<SessionId> sessionId = new ThreadLocal<>();

    private static String ourWebElementClass;

    public static WebDriver getWebDriver() {
        return driverHolder.get();
    }

    public static void setWebDriver(final WebDriver value) {
        driverHolder.set(value);
    }

    public static String getDriverName() {
        return driverName.get();
    }

    public static void setDriverName(String value) {
        setBrowser(value);
        driverName.set(value);
    }

    private static final ThreadLocal<String> bugParameter = new ThreadLocal<>();

    public static ProxyServer getProxyServer() {
        return proxyServer.get();
    }

    public static void setProxyServer(ProxyServer value) {
        proxyServer.set(value);
    }

    public static Proxy getProxy() {
        return proxy.get();
    }

    public static void setProxy(Proxy value) {
        proxy.set(value);
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

    public static void setBugId(String bugId) {
        bugParameter.set(bugId);
    }

    public static String getBugId() {
        return bugParameter.get();
    }

    public static String getOurWebElementClass() {
        return ourWebElementClass;
    }

    public static void setOurWebElementClass(String ourWebElementClass) {
        SeleniumHolder.ourWebElementClass = ourWebElementClass;
    }
}
