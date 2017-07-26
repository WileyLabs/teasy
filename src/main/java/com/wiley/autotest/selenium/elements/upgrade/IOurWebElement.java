package com.wiley.autotest.selenium.elements.upgrade;

import com.wiley.autotest.selenium.elements.upgrade.v3.OurShould;
import com.wiley.autotest.selenium.elements.upgrade.v3.OurWaitFor;
import com.wiley.autotest.selenium.context.OurSearchStrategy;
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

    boolean isStale();

    String getText();

    boolean isDisplayed();

    Point getLocation();

    Dimension getSize();

    Rectangle getRect();

    String getCssValue(String s);

    Locator getLocator();

    OurShould should();

    OurShould should(OurSearchStrategy strategy);

    OurWaitFor waitFor();

    OurWaitFor waitFor(OurSearchStrategy strategy);

    OurWebElement getParent();

    OurWebElement getParent(int level);

    //discuss the necessity of these methods
    OurWebElement element(By by);

    List<OurWebElement> elements(By by);

    OurWebElement elementOrNull(By by);

    List<OurWebElement> elementsOrEmpty(By by);

    OurWebElement domElement(By by);

    List<OurWebElement> domElements(By by);

    void click();
}
