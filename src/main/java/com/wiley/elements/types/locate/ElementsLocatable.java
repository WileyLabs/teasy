package com.wiley.elements.types.locate;

import com.wiley.elements.types.Locatable;
import com.wiley.elements.TeasyElement;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Find element in list by index
 */
public class ElementsLocatable implements Locatable {

    private TeasyElement searchContext;
    private WebDriver driver;
    private By by;
    private int index;

    public ElementsLocatable(TeasyElement searchContext, By by, int index) {
        this.searchContext = searchContext;
        this.by = by;
        this.index = index;
    }

    public ElementsLocatable(WebDriver driver, By by, int index) {
        this.driver = driver;
        this.by = by;
        this.index = index;
    }

    @Override
    public WebElement find() {
        try {
            return driver != null
                    ? driver.findElements(by).get(index)
                    : searchContext.domElements(by).get(index).getWrappedWebElement();
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException("Unable to find element with locator " + by + " and index " + index + ", Exception - " + e);
        }
    }

    @Override
    public By getBy() {
        return by;
    }
}
