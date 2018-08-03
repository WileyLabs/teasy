package com.wiley.elements.types.locate;

import com.wiley.elements.types.Locatable;
import com.wiley.elements.TeasyElement;
import com.wiley.elements.TeasyElementData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LocatableFactory {

    private TeasyElementData elementData;
    private WebDriver driver;

    public LocatableFactory(TeasyElementData elementData, WebDriver driver) {
        this.elementData = elementData;
        this.driver = driver;
    }

    public Locatable get() {
        TeasyElement searchContext = elementData.getSearchContext();
        By by = elementData.getBy();
        Integer index = elementData.getIndex();
        WebElement element = elementData.getElement();
        Locatable locatable;
        if (searchContext != null && by != null && index != null) {
            //element from list in searchContext with index
            locatable = new ElementsLocatable(searchContext, by, index);
        } else if (by != null && index != null) {   //element from list with index
            locatable = new ElementsLocatable(driver, by, index);
        } else if (searchContext != null && by != null) {
            //element in searchContext
            locatable = new ElementLocatable(searchContext, by);
        } else if (by != null) {
            //element
            locatable = new ElementLocatable(driver, by);
        } else {
            //parent element (locatable is null - as a sign that we should take parent)
            TeasyElement ourWebElement = (TeasyElement) element;
            locatable = new ParentElementLocatable(driver, ourWebElement.getLocatable().getBy());
        }
        return locatable;
    }
}
