package com.wiley.autotest.selenium.elements.upgrade;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ElementLocatorFactory {

    private TeasyElementData elementData;
    private WebDriver driver;

    ElementLocatorFactory(TeasyElementData elementData, WebDriver driver) {
        this.elementData = elementData;
        this.driver = driver;
    }

    public Locator getLocator() {
        TeasyElement searchContext = elementData.getSearchContext();
        By by = elementData.getBy();
        Integer index = elementData.getIndex();
        WebElement element = elementData.getElement();
        if (searchContext != null && by != null && index != null) {
            //element from list in searchContext with index
            return new FindElementsLocator(searchContext, by, index);
        } else if (by != null && index != null) {   //element from list with index
            return new FindElementsLocator(driver, by, index);
        } else if (searchContext != null && by != null) {
            //element in searchContext
           return new FindElementLocator(searchContext, by);
        } else if (by != null) {
            //element
            return new FindElementLocator(driver, by);
        } else {
            //parent element (locator is null - as a sign that we should take parent)
            TeasyElement ourWebElement = (TeasyElement) element;
            return new FindParentElementLocator(driver, ourWebElement.getLocator().getLocator());
        }
    }
}
