package com.wiley.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Class-holder for all data that is used for TeasyElement creation;
 */
public class TeasyElementData {

    //element that we found and will be wrapping with TeasyElement;
    private final WebElement element;
    //when we search for an element inside another element;
    private final TeasyElement context;
    //locator for an element that we are searching for;
    private final By by;
    //index of a particular element in a list;
    private final Integer index;

    public TeasyElementData(WebElement element, By by) {
        this(element, by, null, null);
    }

    public TeasyElementData(WebElement element, By by, int index) {
        this(element, by, null, index);
    }

    public TeasyElementData(TeasyElement context, WebElement element, By by) {
        this(element, by, context, null);
    }

    public TeasyElementData(WebElement element, By by, TeasyElement context, Integer index) {
        this.context = context;
        this.element = element;
        this.by = by;
        this.index = index;
    }

    public WebElement getElement() {
        return element;
    }

    public TeasyElement getSearchContext() {
        return context;
    }

    public By getBy() {
        return by;
    }

    public Integer getIndex() {
        return index;
    }
}
