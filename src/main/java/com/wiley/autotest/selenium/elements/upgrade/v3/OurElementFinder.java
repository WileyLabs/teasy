package com.wiley.autotest.selenium.elements.upgrade.v3;

import com.wiley.autotest.selenium.context.SearchStrategy;
import com.wiley.autotest.selenium.elements.upgrade.TeasyWebElement;
import com.wiley.autotest.selenium.elements.upgrade.v3.conditions.FramesConditionFactory;
import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.wiley.autotest.selenium.elements.upgrade.CustomWebElementFactory.wrap;
import static com.wiley.autotest.selenium.elements.upgrade.CustomWebElementFactory.wrapList;


/**
 * Finds different kinds of elements for example
 * visible, present in dom, alerts etc.
 */
public class OurElementFinder {

    private SearchStrategy strategy;

    private TeasyWebElement context;

    private OurFluentWait<WebDriver> fluentWait;

    public OurElementFinder(WebDriver driver, SearchStrategy strategy) {
        this.strategy = strategy;
        this.fluentWait = new OurFluentWait<>(driver, strategy);
    }

    public OurElementFinder(WebDriver driver, SearchStrategy strategy, TeasyWebElement context) {
        this(driver, strategy);
        this.context = context;
    }

    public TeasyWebElement visibleElement(By locator) {
        Function<WebDriver, List<WebElement>> condition = new FramesConditionFactory(context).get(strategy.getFrameStrategy())
                .visibility(locator);
        return getElement(locator, condition);
    }

    /**
     * note: Will return empty list in case no visible elements found
     */
    public List<TeasyWebElement> visibleElements(By locator) {
        Function<WebDriver, List<WebElement>> condition = new FramesConditionFactory(context).get(strategy.getFrameStrategy())
                .visibility(locator);
        return getElements(locator, condition);
    }

    public TeasyWebElement presentInDomElement(By locator) {
        Function<WebDriver, List<WebElement>> condition = new FramesConditionFactory(context).get(strategy.getFrameStrategy())
                .presence(locator);
        return getElement(locator, condition);
    }

    /**
     * note: Will return empty list in case no visible elements found
     */
    public List<TeasyWebElement> presentInDomElements(By locator) {
        Function<WebDriver, List<WebElement>> condition = new FramesConditionFactory(context).get(strategy.getFrameStrategy())
                .presence(locator);
        return getElements(locator, condition);
    }

    //this is not "OUR" alert yet, so should probably become "OUR"
    public Alert alert() {
        return fluentWait.waitFor(ExpectedConditions.alertIsPresent());
    }


    private List<WebElement> waitFor(Function<WebDriver, List<WebElement>> condition) {
        return fluentWait.waitFor(condition);
    }

    private List<TeasyWebElement> getElements(By locator, Function<WebDriver, List<WebElement>> condition) {
        List<WebElement> webElements;
        try {
            webElements = waitFor(condition);
        } catch (AssertionError ignoredToReturnEmptyList) {
            return new ArrayList<>();
        }
        if (webElements == null) {
            return new ArrayList<>();
        }
        if (context == null) {
            return wrapList(webElements, locator);
        } else {
            return wrapList(context, webElements, locator);
        }
    }

    @Nullable
    private TeasyWebElement getElement(By locator, Function<WebDriver, List<WebElement>> condition) {
        List<WebElement> webElements = waitFor(condition);
        if (webElements == null) {
            return null;
        }
        if (context == null) {
            return wrap(webElements.get(0), locator);
        } else {
            return wrap(context, webElements.get(0), locator);
        }
    }
}
