package com.wiley.autotest.selenium.elements.upgrade;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * User: ntyukavkin
 * Date: 18.11.2016
 * Time: 16:34
 */
public class TeasyWebElementData {

    private WebElement element;
    private TeasyWebElement searchContext;
    private By by;
    private Integer index;
    private Locator locator;

    public TeasyWebElementData(WebElement element) {
        this.element = element;
    }

    public TeasyWebElementData(WebElement element, By by) {
        this(element);
        this.by = by;
    }

    public TeasyWebElementData(TeasyWebElement context, WebElement element, By by) {
        this(element, by);
        this.searchContext = context;
    }

    public TeasyWebElementData(TeasyWebElement context, WebElement element, By by, int index) {
        this(context, element, by);
        this.index = index;
    }

    public WebElement getElement() {
        return element;
    }

    public void setElement(WebElement element) {
        this.element = element;
    }

    public TeasyWebElement getSearchContext() {
        return searchContext;
    }

    public void setSearchContext(TeasyWebElement searchContext) {
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
