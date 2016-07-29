package com.wiley.autotest.selenium.driver.events.listeners;

import com.wiley.autotest.selenium.context.PageLoadingValidator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

public class PageValidatorEventListener extends AbstractWebDriverEventListener {
    private final PageLoadingValidator validator;

    public PageValidatorEventListener(final PageLoadingValidator validator) {
        this.validator = validator;
    }

    @Override
    public void afterClickOn(final WebElement element, final WebDriver driver) {
        validator.assertLoaded(driver);
    }

    @Override
    public void afterNavigateTo(final String url, final WebDriver driver) {
        validator.assertLoaded(driver);
    }

    @Override
    public void afterNavigateBack(final WebDriver driver) {
        validator.assertLoaded(driver);
    }

    @Override
    public void afterNavigateForward(final WebDriver driver) {
        validator.assertLoaded(driver);
    }
}
