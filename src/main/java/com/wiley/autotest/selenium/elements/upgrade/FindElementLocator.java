package com.wiley.autotest.selenium.elements.upgrade;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Find element
 */
public class FindElementLocator implements Locator {
    private TeasyElement searchContext;
    private WebDriver driver;
    private By by;

    public FindElementLocator(TeasyElement searchContext, By by) {
        this.searchContext = searchContext;
        this.by = by;
    }

    public FindElementLocator(WebDriver driver, By by) {
        this.driver = driver;
        this.by = by;
    }

    @Override
    public WebElement find() {
        return driver != null ? driver.findElement(by) : searchContext.domElement(by).getWrappedWebElement();
    }

    @Override
    public By getLocator() {
        return by;
    }
}
