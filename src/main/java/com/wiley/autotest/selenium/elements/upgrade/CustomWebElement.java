package com.wiley.autotest.selenium.elements.upgrade;

import com.wiley.autotest.selenium.elements.upgrade.v3.ElementWaitFor;
import com.wiley.autotest.selenium.elements.upgrade.v3.OurShould;
import com.wiley.autotest.selenium.context.SearchStrategy;
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

    OurShould should(SearchStrategy strategy);

    ElementWaitFor waitFor();

    ElementWaitFor waitFor(SearchStrategy strategy);

    OurWebElement getParent();

    OurWebElement getParent(int level);

    OurWebElement element(By by);
    OurWebElement element(By by, SearchStrategy strategy);

    List<OurWebElement> elements(By by);
    List<OurWebElement> elements(By by, SearchStrategy strategy);

    OurWebElement domElement(By by);
    OurWebElement domElement(By by, SearchStrategy strategy);

    List<OurWebElement> domElements(By by);
    List<OurWebElement> domElements(By by, SearchStrategy strategy);

    void click();
}
