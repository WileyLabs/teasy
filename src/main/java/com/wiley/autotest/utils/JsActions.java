package com.wiley.autotest.utils;

import com.wiley.autotest.selenium.SeleniumHolder;
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
    public static void executeScript(String script, Object... args) {
        driver().executeScript(script, args);
    }

    /**
     * {@link JavascriptExecutor#executeAsyncScript(String, Object...)}
     */
    public static void executeAsyncScript(String script, Object... args) {
        driver().executeAsyncScript(script, args);
    }

    private static JavascriptExecutor driver() {
        return (JavascriptExecutor) SeleniumHolder.getWebDriver();
    }
}
