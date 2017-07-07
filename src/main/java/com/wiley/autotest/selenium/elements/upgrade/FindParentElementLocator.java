package com.wiley.autotest.selenium.elements.upgrade;

import com.wiley.autotest.utils.ExecutionUtils;
import org.openqa.selenium.*;

/**
 * User: vefimov
 * Date: 27.08.2014
 * Time: 15:50
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
        if (ExecutionUtils.isSafari()) {
            return searchContext.findElement(by).findElement(By.xpath("./.."));
        } else {
            return (WebElement) ((JavascriptExecutor) searchContext).executeScript("return arguments[0].parentNode", searchContext.findElement(by));
        }
    }

    @Override
    public By getLocator() {
        return by;
    }
}
