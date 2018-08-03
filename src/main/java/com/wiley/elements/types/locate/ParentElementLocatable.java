package com.wiley.elements.types.locate;

import com.wiley.elements.types.Locatable;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import static com.wiley.utils.JsActions.executeScript;

/**
 * Find parent element
 */
public class ParentElementLocatable implements Locatable {

    private SearchContext searchContext;
    private By by;

    public ParentElementLocatable(SearchContext searchContext, By by) {
        this.searchContext = searchContext;
        this.by = by;
    }

    @Override
    public WebElement find() {
        return (WebElement) executeScript("return arguments[0].parentNode", searchContext.findElement(by));
    }

    @Override
    public By getBy() {
        return by;
    }
}
