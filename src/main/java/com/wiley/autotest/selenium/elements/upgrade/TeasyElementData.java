package com.wiley.autotest.selenium.elements.upgrade;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Class-holder for all data that is used fo future TeasyElement creation;
 */
public class TeasyElementData {

    //element that we found and will be wrapping with TeasyElement;
    private WebElement element;
    //when we search for an element inside another element;
    private TeasyElement searchContext;
    //locator for an element that we are searching for;
    private By by;
    //index of a particular element in a list;
    private Integer index;

    public TeasyElementData(WebElement element, By by) {
        this.element = element;
        this.by = by;
    }

    public TeasyElementData(WebElement element, By by, int index) {
        this(element, by);
        this.index = index;
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

    public TeasyElement getSearchContext() {
        return searchContext;
    }

    public By getBy() {
        return by;
    }

    public Integer getIndex() {
        return index;
    }

}
