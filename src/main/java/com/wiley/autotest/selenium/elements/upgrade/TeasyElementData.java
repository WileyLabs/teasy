package com.wiley.autotest.selenium.elements.upgrade;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * User: ntyukavkin
 * Date: 18.11.2016
 * Time: 16:34
 */
public class TeasyElementData {

    private WebElement element;
    private TeasyElement searchContext;
    private By by;
    private Integer index;
    private Locator locator;

    public TeasyElementData(WebElement element) {
        this.element = element;
    }

    public TeasyElementData(WebElement element, By by) {
        this(element);
        this.by = by;
    }

    public TeasyElementData(TeasyElement context, WebElement element, By by) {
        this(element, by);
        this.searchContext = context;
    }

    public TeasyElementData(TeasyElement context, WebElement element, By by, int index) {
        this(context, element, by);
        this.index = index;
    }

    public WebElement getElement() {
        return element;
    }

    public void setElement(WebElement element) {
        this.element = element;
    }

    public TeasyElement getSearchContext() {
        return searchContext;
    }

    public void setSearchContext(TeasyElement searchContext) {
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
