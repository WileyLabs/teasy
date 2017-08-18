package com.wiley.autotest.selenium.elements.upgrade;

import com.wiley.autotest.selenium.context.OurSearchStrategy;
import com.wiley.autotest.selenium.elements.upgrade.v3.OurShould;
import com.wiley.autotest.selenium.elements.upgrade.v3.OurWaitFor;
import org.openqa.selenium.*;

import java.util.List;

/**
 * User: vefimov
 * Date: 28.08.2014
 * Time: 18:43
 */
public interface CustomWebElement {

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

    OurWebElement element(By by);
    OurWebElement element(By by, OurSearchStrategy strategy);

    OurWebElement elementOrNull(By by);

    List<OurWebElement> elementsOrEmptyList(By by, OurSearchStrategy strategy);

    List<OurWebElement> elements(By by);
    List<OurWebElement> elements(By by, OurSearchStrategy strategy);

    OurWebElement domElement(By by);
    OurWebElement domElement(By by, OurSearchStrategy strategy);

    List<OurWebElement> domElements(By by);
    List<OurWebElement> domElements(By by, OurSearchStrategy strategy);

    void click();
}
