package com.wiley.autotest.selenium.elements.upgrade;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * User: vefimov
 * Date: 27.08.2014
 * Time: 15:50
 */
public class FindElementLocator implements Locator {
    private SearchContext searchContext;
    private By by;

    public FindElementLocator(SearchContext searchContext, By by) {
        this.searchContext = searchContext;
        this.by = by;
    }

    @Override
    public WebElement locate() {
        return searchContext.findElement(by);
    }

    @Override
    public By getByLocator() {
        return by;
    }
}
