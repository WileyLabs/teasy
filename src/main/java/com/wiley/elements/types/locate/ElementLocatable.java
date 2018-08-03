package com.wiley.elements.types.locate;

import com.wiley.elements.types.Locatable;
import com.wiley.elements.TeasyElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Find element
 */
public class ElementLocatable implements Locatable {

    private TeasyElement searchContext;
    private WebDriver driver;
    private By by;

    public ElementLocatable(TeasyElement searchContext, By by) {
        this.searchContext = searchContext;
        this.by = by;
    }

    public ElementLocatable(WebDriver driver, By by) {
        this.driver = driver;
        this.by = by;
    }

    @Override
    public WebElement find() {
        return driver != null
                ? driver.findElement(by)
                : searchContext.domElement(by).getWrappedWebElement();
    }

    @Override
    public By getBy() {
        return by;
    }
}
