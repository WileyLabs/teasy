package com.wiley.autotest.selenium.elements.upgrade;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * User: vefimov
 * Date: 27.08.2014
 * Time: 15:51
 */
public class FindElementsLocator implements Locator {
    private TeasyElement searchContext;
    private WebDriver driver;
    private By by;
    private int index;

    public FindElementsLocator(TeasyElement searchContext, By by, int index) {
        this.searchContext = searchContext;
        this.by = by;
        this.index = index;
    }

    public FindElementsLocator(WebDriver driver, By by, int index) {
        this.driver = driver;
        this.by = by;
        this.index = index;
    }

    @Override
    public WebElement find() {
        try {
            return driver != null ? driver.findElements(by).get(index) : searchContext.domElements(by).get(index).getWrappedWebElement();
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException("Unable to find element " + by + ", Exception - " + e);
        }
    }

    @Override
    public By getLocator() {
        return by;
    }
}
