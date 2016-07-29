package com.wiley.autotest.event.postpone.failure;

import com.wiley.autotest.screenshots.Screenshoter;

import static org.testng.Reporter.log;

public class ScreenshotOnPostponeFailureSubscriber extends PostponeFailureSubscriber {
    private final String testName;
    private final Screenshoter screenshoter = new Screenshoter();
    private int failures = 0;

    public ScreenshotOnPostponeFailureSubscriber(String testName) {
        this.testName = testName;
    }

    @Override
    public void notify(final String message) {
        try {
            screenshoter.takeScreenshot(message, String.format("%s.%d", testName, ++failures));
        } catch (Exception e) {
            log("Couldn't take screenshot. Error: " + e.getMessage());
        }
    }
}
