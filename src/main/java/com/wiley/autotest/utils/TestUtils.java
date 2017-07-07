package com.wiley.autotest.utils;

import com.wiley.autotest.annotations.Bug;
import com.wiley.autotest.spring.SeleniumTestExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITest;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

public final class TestUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumTestExecutionListener.class);

    private TestUtils() {
    }

    public static void fail(final String errorMessage) {
        Reporter.log("ERROR: " + errorMessage);
        throw new AssertionError(errorMessage);
    }

    public static String getTestName(final ITestResult test) {
        return getTestName(test.getMethod().getConstructorOrMethod().getMethod());
    }

    public static String getTestName(Method testMethod) {
        String testName = testMethod.getName();
        Bug bugAnnotation = testMethod.getAnnotation(Bug.class);
        if (bugAnnotation != null) {
            testName = testName + "_HAS_BUG_ANNOTATION_WITH_ID_" + bugAnnotation.id();
        }
        return testName;
    }

    public static String getBoldValue() {
        if (ExecutionUtils.isChrome()) {
            return "bold";
        } else {
            return "700";
        }
    }

    /**
     * Do not use this method!
     * Ok at least try to avoid using it.
     * This is usually used as a workaround.
     * Every place of code where this method is used is potentially a weak place.
     *
     * @param milliseconds
     * @param explanationMessage
     */
    public static void waitForSomeTime(int milliseconds, String explanationMessage) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new AssertionError("InterruptedException occurred while calling Thread.sleep for " + milliseconds + " milliseconds. " + explanationMessage, e);
        }
    }

    @Deprecated
    public static void waitForSomeTime(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new AssertionError("InterruptedException occurred while calling Thread.sleep for " + milliseconds + " milliseconds.", e);
        }
    }


    /**
     * Custom wait for Mac Safari yet we haven't found the better way to work with it
     */
    public static void waitForSafari() {
        waitForSomeTime(3000, "Special wait for Safari");
    }

    /**
     * For running tests in parallel the key have to be unique for every Thread
     *
     * @param key - default value of the key
     * @return modified value of key (key + "." + id of current Thread)
     */
    public static String modifyKeyForCurrentThread(final String key) {
        return key + "." + Thread.currentThread().getId();
    }

    /**
     * Get location of temp directory on local pc or jenkins
     *
     * @return - location as String or null if file was not found
     */

    public static String getTempDirectoryLocation() {
        File file;
        try {
            file = File.createTempFile("upload", ".txt");
            String location = file.getParent();
            file.deleteOnExit();
            return location;
        } catch (IOException e) {
            LOGGER.error("Error while getting TempDirectory + " + e.getMessage());
        }
        return null;
    }

    public static String getTestCaseNumber(ITest testClass) {
        String testName = testClass.getTestName();
        return testName.contains("E4_") ? testName.split("E4")[1].split("_")[1] :
                testName.split("TC")[1].split("_")[1];
    }
}
