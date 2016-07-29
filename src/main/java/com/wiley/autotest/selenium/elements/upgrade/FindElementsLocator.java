package com.wiley.autotest.selenium.elements.upgrade;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * User: vefimov
 * Date: 27.08.2014
 * Time: 15:51
 */
public class FindElementsLocator implements Locator {
    private SearchContext searchContext;
    private By by;
    private int index;

    public FindElementsLocator(SearchContext searchContext, By by, int index) {
        this.searchContext = searchContext;
        this.by = by;
        this.index = index;
    }

    @Override
    public WebElement locate() {
        return searchContext.findElements(by).get(index);
    }

    @Override
    public By getByLocator() {
        return by;
    }
}
