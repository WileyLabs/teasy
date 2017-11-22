package com.wiley.autotest.selenium.elements.upgrade;

import com.wiley.autotest.selenium.context.SearchStrategy;
import com.wiley.autotest.selenium.elements.upgrade.should.Should;
import com.wiley.autotest.selenium.elements.upgrade.waitfor.ElementWaitFor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public interface TeasyElement extends WebElement {

    /**
     * Do not use this method. it does not fit TeasyElement concept and only here because
     * it is originally a method of WebElement.
     * use elements() or domElements(), depending on your needs
     */
    @Override
    @Deprecated
    List<WebElement> findElements(By by);

    /**
     * Do not use this method. it does not fit TeasyElement concept and only here because
     * it is originally a method of WebElement
     * use element() or domElement() depending on your needs
     */
    @Override
    @Deprecated
    WebElement findElement(By by);

    WebElement getWrappedWebElement();

    boolean isStale();

    Locator getLocator();

    Should should();

    Should should(SearchStrategy strategy);

    ElementWaitFor waitFor();

    ElementWaitFor waitFor(SearchStrategy strategy);

    TeasyElement getParent();

    TeasyElement getParent(int level);

    TeasyElement element(By by);

    TeasyElement element(By by, SearchStrategy strategy);

    TeasyElementList elements(By by);

    TeasyElementList elements(By by, SearchStrategy strategy);

    TeasyElement domElement(By by);

    TeasyElement domElement(By by, SearchStrategy strategy);

    TeasyElementList domElements(By by);

    TeasyElementList domElements(By by, SearchStrategy strategy);
}
