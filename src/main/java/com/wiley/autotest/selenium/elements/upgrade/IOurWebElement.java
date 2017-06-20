package com.wiley.autotest.selenium.elements.upgrade;

import com.wiley.autotest.selenium.elements.upgrade.v3.OurShould;
import com.wiley.autotest.selenium.elements.upgrade.v3.OurWaitFor;
import com.wiley.autotest.selenium.elements.upgrade.v3.OurSearchStrategy;
import org.openqa.selenium.*;

import java.util.List;

/**
 * User: vefimov
 * Date: 28.08.2014
 * Time: 18:43
 */
//public interface IOurWebElement extends WebElement {
public interface IOurWebElement {

    <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException;

    WebElement getWrappedWebElement();

    void submit();

    void sendKeys(CharSequence... charSequences);

    void clear();

    String getTagName();

    String getAttribute(String s);

    boolean isSelected();

    boolean isEnabled();

    String getText();

    boolean isDisplayed();

    Point getLocation();

    Dimension getSize();

    Rectangle getRect();

    String getCssValue(String s);

    Locator getLocator();

    public OurShould should();

    public OurShould should(OurSearchStrategy strategy);

    public OurWaitFor waitFor();

    public OurWaitFor waitFor(OurSearchStrategy strategy);

    public IOurWebElement getParent();

    public IOurWebElement getParent(int level);

    //discuss the necessity of these methods
    public OurWebElement element(By by);

    public List<OurWebElement> elements(By by);

    public OurWebElement elementOrNull(By by);

    public List<OurWebElement> elementsOrEmpty(By by);

    public OurWebElement domElement(By by);

    public List<OurWebElement> domElements(By by);

    public void click();
}
