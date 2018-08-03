package com.wiley.utils;

import io.qameta.allure.Step;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.LoggerFactory;

/**
 * Transfers message to necessary end point
 * TestNG
 * Allure
 * Jenkins console
 */
public final class Report {

    private Report() {
    }

    public static void everywhere(String message) {
        testNG(message);
        allure(message);
        jenkins(message);
    }

    public static void testNG(String message) {
        org.testng.Reporter.log(message + "<br/>");
    }

    public static void allure(String message) {
        allureHack(message);
    }

    /**
     * hack to send message to allure
     *
     * @param message - is used by {@link Step} annotation
     */
    @Step("{0}")
    private static void allureHack(String message) {
    }

    public static void jenkins(String message) {
        LoggerFactory.getLogger(Report.class).info(message);
    }

    public static void jenkins(String message, Throwable throwable) {
        LoggerFactory.getLogger(Report.class).info(message + "\n" + ExceptionUtils.getStackTrace(throwable));
    }
}
