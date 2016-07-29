package com.wiley.autotest.selenium.elements.upgrade;

import org.openqa.selenium.WebElement;

/**
 * User: vefimov
 * Date: 28.08.2014
 * Time: 18:43
 */
public interface OurWebElement extends WebElement {

    public WebElement getWrappedWebElement();

    public Locator getLocator();

}
