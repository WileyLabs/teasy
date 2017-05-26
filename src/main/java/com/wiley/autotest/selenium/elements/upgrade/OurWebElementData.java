package com.wiley.autotest.selenium.elements.upgrade;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * User: ntyukavkin
 * Date: 18.11.2016
 * Time: 16:34
 */
public class OurWebElementData {

    private WebElement element;
    private SearchContext searchContext;
    private By by;
    private Integer index;
    private Locator locator;

    public OurWebElementData(WebElement element) {
        this.element = element;
    }

    public OurWebElementData(WebElement element, By by) {
        this(element);
        this.by = by;
    }

    public OurWebElementData(SearchContext context, WebElement element, By by) {
        this(element, by);
        this.searchContext = context;
    }

    public OurWebElementData(SearchContext context, WebElement element, By by, int index) {
        this(context, element, by);
        this.index = index;
    }

    public WebElement getElement() {
        return element;
    }

    public void setElement(WebElement element) {
        this.element = element;
    }

    public SearchContext getSearchContext() {
        return searchContext;
    }

    public void setSearchContext(SearchContext searchContext) {
        this.searchContext = searchContext;
    }

    public By getBy() {
        return by;
    }

    public void setBy(By by) {
        this.by = by;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Locator getLocator() {
        return locator;
    }

    public void setLocator(Locator locator) {
        this.locator = locator;
    }
}
