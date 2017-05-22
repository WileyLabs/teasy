package com.wiley.autotest.selenium.elements.upgrade;

import com.wiley.autotest.selenium.elements.upgrade.v3.OurShould;
import com.wiley.autotest.selenium.elements.upgrade.v3.OurWaitFor;
import com.wiley.autotest.selenium.elements.upgrade.v3.SearchStrategy;
import org.openqa.selenium.WebElement;

/**
 * User: vefimov
 * Date: 28.08.2014
 * Time: 18:43
 */
public interface IOurWebElement extends WebElement {

    WebElement getWrappedWebElement();

    Locator getLocator();

    public OurShould should();

    public OurShould should(SearchStrategy strategy);

    public OurWaitFor waitFor();

    public OurWaitFor waitFor(SearchStrategy strategy);

    public IOurWebElement getParent();

    public IOurWebElement getParent(int level);

    //discuss the necessity of these methods
//    public OurWebElement element(By locator);
//
//    public OurWebElement elements(By locator);
}
