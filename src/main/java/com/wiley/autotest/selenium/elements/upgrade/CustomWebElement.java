package com.wiley.autotest.selenium.elements.upgrade;

import com.wiley.autotest.selenium.elements.upgrade.v3.ElementWaitFor;
import com.wiley.autotest.selenium.elements.upgrade.v3.Should;
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

    Should should();

    Should should(SearchStrategy strategy);

    ElementWaitFor waitFor();

    ElementWaitFor waitFor(SearchStrategy strategy);

    TeasyElement getParent();

    TeasyElement getParent(int level);

    TeasyElement element(By by);
    TeasyElement element(By by, SearchStrategy strategy);

    List<TeasyElement> elements(By by);
    List<TeasyElement> elements(By by, SearchStrategy strategy);

    TeasyElement domElement(By by);
    TeasyElement domElement(By by, SearchStrategy strategy);

    List<TeasyElement> domElements(By by);
    List<TeasyElement> domElements(By by, SearchStrategy strategy);

    void click();
}
