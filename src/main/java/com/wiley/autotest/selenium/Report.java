package com.wiley.autotest.selenium;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.testng.Reporter;
import io.qameta.allure.Step;

/**
 * Transfers message to necessary end point
 * TestNG
 * Allure
 * Jenkins console
 */
public class Report {

    private String message;
    private Level level = Level.INFO;

    public Report(String message) {
        this.message = message;
    }

    public Report(String message, Level level) {
        this.message = message;
        this.level = level;
    }

    public Report(String message, Throwable t) {
        this(message + "\n" + ExceptionUtils.getStackTrace(t));
    }

    public Report(String message, Throwable t, Level level) {
        this(message + "\n" + ExceptionUtils.getStackTrace(t), level);
    }

    public void testNG() {
        Reporter.log(message + "<br/>");
    }

    public void allure() {
        allure(message);
    }

    /**
     * hack to send message to allure
     */
    @Step("{0}")
    private void allure(String message) {
    }

    public void jenkins() {
        switch (level) {
            case INFO:
                LoggerFactory.getLogger(this.getClass()).info(message);
                break;
            case WARN:
                LoggerFactory.getLogger(this.getClass()).warn(message);
                break;
            case DEBUG:
                LoggerFactory.getLogger(this.getClass()).debug(message);
                break;
            case ERROR:
                LoggerFactory.getLogger(this.getClass()).error(message);
                break;
        }
    }

    public void everywhere() {
        testNG();
        allure();
        jenkins();
    }
}
