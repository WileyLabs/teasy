package com.wiley.autotest.selenium.elements.upgrade;

import com.wiley.autotest.utils.ExecutionUtils;
import org.openqa.selenium.*;

/**
 * Find parent element
 */
public class FindParentElementLocator implements Locator {
    private SearchContext searchContext;
    private By by;

    public FindParentElementLocator(SearchContext searchContext, By by) {
        this.searchContext = searchContext;
        this.by = by;
    }

    @Override
    public WebElement find() {
        return (WebElement) ((JavascriptExecutor) searchContext).executeScript("return arguments[0].parentNode", searchContext.findElement(by));
    }

    @Override
    public By getLocator() {
        return by;
    }
}
