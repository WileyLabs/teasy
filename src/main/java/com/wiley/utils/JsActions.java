package com.wiley.utils;

import com.wiley.holders.DriverHolder;
import org.openqa.selenium.JavascriptExecutor;

/**
 * Javascript actions through WebDriver
 * A wrapper around WebDriver taken from a SeleniumHolder
 */
public final class JsActions {

    private JsActions() {
    }

    /**
     * {@link JavascriptExecutor#executeScript(String, Object...)}
     */
    public static Object executeScript(String script, Object... args) {
        return driver().executeScript(script, args);
    }

    /**
     * {@link JavascriptExecutor#executeAsyncScript(String, Object...)}
     */
    public static Object executeAsyncScript(String script, Object... args) {
        return driver().executeAsyncScript(script, args);
    }

    private static JavascriptExecutor driver() {
        return (JavascriptExecutor) DriverHolder.getDriver();
    }
}
