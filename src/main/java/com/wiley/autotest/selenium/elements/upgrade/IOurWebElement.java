package com.wiley.autotest.selenium.elements.upgrade;

import com.wiley.autotest.selenium.elements.upgrade.v3.OurShould;
import com.wiley.autotest.selenium.elements.upgrade.v3.OurWaitFor;
import com.wiley.autotest.selenium.elements.upgrade.v3.OurSearchStrategy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * User: vefimov
 * Date: 28.08.2014
 * Time: 18:43
 */
public interface IOurWebElement extends WebElement {

    WebElement getWrappedWebElement();

    Locator getLocator();

    public OurShould should();

    public OurShould should(OurSearchStrategy strategy);

    public OurWaitFor waitFor();

    public OurWaitFor waitFor(OurSearchStrategy strategy);

    public IOurWebElement getParent();

    public IOurWebElement getParent(int level);

    //discuss the necessity of these methods
    public WebElement element(By by);

    public List<WebElement> elements(By by);

    public WebElement elementOrNull(By by);

    public List<WebElement> elementsOrEmpty(By by);

    public WebElement domElement(By by);

    public List<WebElement> domElements(By by);
}
