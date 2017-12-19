package com.wiley.autotest.spring;

import org.apache.commons.lang.StringUtils;

import java.util.Properties;

/**
 * @author alexey.a.semenov@gmail.com
 */
public class Settings extends Properties {

    private static final String APPLICATION_HOST_PROP_KEY = "application.host";
    private static final String APPLICATION_PORT_PROP_KEY = "application.port";
    private static final String APPLICATION_PROTOCOL_PROP_KEY = "application.protocol";
    private static final String APPLICATION_CONTEXT_PROP_KEY = "application.context";
    private static final String APPLICATION_SELENIUM_DRIVER_PROP_KEY = "application.selenium.driver";
    private static final String APPLICATION_SELENIUM_TIMEOUT_PROP_KEY = "application.selenium.timeout";
    private static final String SELENIUM_FIREFOX_PROFILE_PROP_KEY = "selenium.firefox.profile";
    private static final String RUN_TESTS_WITH_GRID_PROP_KEY = "run.tests.with.grid";
    private static final String GRID_HUB_URL_PROP_KEY = "grid.hub.url";
    private static final String APPLICATION_PLATFORM_PROP_KEY = "application.platform";
    private static final String INVOCATION_COUNT_PROP_KEY = "invocation.count";
    private static final String RESTART_DRIVER_COUNT_PROP_KEY = "restart.driver.count";
    private static final String HEADLESS_BROWSER_PROP_KEY = "headless.browser";

    public boolean getAsBoolean(String name) {
        return Boolean.valueOf(getProperty(name));
    }

    public int getAsInteger(String name) {
        return Integer.valueOf(getProperty(name));
    }

    public String getContext() {
        return getProperty(APPLICATION_CONTEXT_PROP_KEY);
    }

    public String getHost() {
        return getProperty(APPLICATION_HOST_PROP_KEY);
    }

    public String getHostWithPort() {
        return getHost() + (getPort() == 80 ? "" : ":" + getPort());
    }

    private int getPort() {
        return getAsInteger(APPLICATION_PORT_PROP_KEY);
    }

    public String getProtocol() {
        return getProperty(APPLICATION_PROTOCOL_PROP_KEY);
    }

    public String getFullHost() {
        return getProtocol() + getHostWithPort();
    }

    public String createUrl() {
        return createUrl("");
    }

    public String createUrl(final String path) {
        String context = getContext();
        return String.format("%s%s%s",
                getFullHost(),
                StringUtils.isBlank(context) ? "" : "/" + context,
                path);
    }

    public String getDriverName() {
        return getProperty(APPLICATION_SELENIUM_DRIVER_PROP_KEY);
    }

    public Integer getTimeout() {return  getAsInteger(APPLICATION_SELENIUM_TIMEOUT_PROP_KEY);}

    public String getBrowserProfileName() {
        return getProperty(SELENIUM_FIREFOX_PROFILE_PROP_KEY);
    }

    public boolean isRunTestsWithGrid() {
        return getAsBoolean(RUN_TESTS_WITH_GRID_PROP_KEY);
    }

    public String getGridHubUrl() {
        return getProperty(GRID_HUB_URL_PROP_KEY);
    }

    public String getPlatform() {
        return getProperty(APPLICATION_PLATFORM_PROP_KEY);
    }

    public int getInvocationCount() {
        return getAsInteger(INVOCATION_COUNT_PROP_KEY);
    }

    public void setDriverName(String driverName) {
        setProperty(APPLICATION_SELENIUM_DRIVER_PROP_KEY, driverName);
    }

    public Integer getRestartDriverCount() {
        return getAsInteger(RESTART_DRIVER_COUNT_PROP_KEY);
    }

    public Boolean isHeadlessBrowser() {
        return getAsBoolean(HEADLESS_BROWSER_PROP_KEY);
    }
}
