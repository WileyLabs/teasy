package com.wiley.autotest.screenshots;

import com.wiley.autotest.selenium.driver.events.listeners.ScreenshotWebDriverEventListener;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Created by mosadchiy on 26.05.2016.
 */

public interface ScreenshotProvider {
    Substitution VERSION_SUBSTITUTED_ELEMENT = new Substitution<By>(By.cssSelector("table[id='wp-footer'] > tbody > tr > td:nth-child(2) > div"), "Version 1.00.0");

    void setEventListener(ScreenshotWebDriverEventListener eventListener);
    List<ScreenshotLocator> getIncludeLocators();
    List<ScreenshotLocator> getExcludeLocators();
    List<Substitution> getSubstitutions();
    void beforeScreenshot(WebDriver driver);
    void afterScreenshot(WebDriver driver);
    int getMaxWindowHeight(WebDriver driver);
    BufferedImage cut(WebDriver driver, BufferedImage image);
}
