package com.wiley.autotest.screenshots;

import com.wiley.autotest.selenium.driver.events.listeners.ScreenshotWebDriverEventListener;
import org.openqa.selenium.WebDriver;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Created by mosadchiy on 26.05.2016.
 */

public class InsertProvider implements ScreenshotProvider {
    private ScreenshotProvider baseProvider;
    private List<ScreenshotLocator> insertedLocators;

    public InsertProvider(ScreenshotProvider baseProvider, List<ScreenshotLocator> insertedLocators) {
        this.baseProvider = baseProvider;
        this.insertedLocators = insertedLocators;
    }

    public List<ScreenshotLocator> getInsertedLocators() {
        return insertedLocators;
    }

    @Override
    public void setEventListener(ScreenshotWebDriverEventListener eventListener) {
        baseProvider.setEventListener(eventListener);
    }

    @Override
    public List<ScreenshotLocator> getIncludeLocators() {
        return null;
    }

    @Override
    public List<ScreenshotLocator> getExcludeLocators() {
        return null;
    }

    @Override
    public List<Substitution> getSubstitutions() {
        return baseProvider.getSubstitutions();
    }

    @Override
    public void beforeScreenshot(WebDriver driver) {
        baseProvider.beforeScreenshot(driver);
    }

    @Override
    public void afterScreenshot(WebDriver driver) {
        baseProvider.afterScreenshot(driver);
    }

    @Override
    public int getMaxWindowHeight(WebDriver driver) {
        return baseProvider.getMaxWindowHeight(driver);
    }

    @Override
    public BufferedImage cut(WebDriver driver, BufferedImage image) {
        return baseProvider.cut(driver, image);
    }
}
