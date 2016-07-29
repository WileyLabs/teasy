package com.wiley.autotest.selenium.driver.events.listeners;

import com.wiley.autotest.utils.DriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

/**
 * Created by mosadchiy on 26.05.2016.
 */

public class ScreenshotWebDriverEventListener extends AbstractWebDriverEventListener {
    private WebDriver webDriver;
    private WebElement lastClickedElement;

    @Override
    public void afterClickOn(WebElement element, WebDriver driver) {
        lastClickedElement = element;
        webDriver = driver;
    }

    public void beforeScreenshot() {
        if (lastClickedElement != null) {
            try {
                if (!lastClickedElement.isDisplayed()) {
                    throw new StaleElementReferenceException("");
                }
            } catch (StaleElementReferenceException ex) {
                // Relocates mouse if last clicked element is not longer displayed
                DriverUtils.moveMousePositionTo(webDriver, By.tagName("html"));
            }
        }
    }
}