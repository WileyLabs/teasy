package com.wiley.autotest.selenium.elements.upgrade;

import com.wiley.autotest.selenium.context.SearchStrategy;
import com.wiley.autotest.selenium.elements.upgrade.conditions.FramesConditionFactory;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.wiley.autotest.selenium.elements.upgrade.TeasyElementType.DOM;
import static com.wiley.autotest.selenium.elements.upgrade.TeasyElementType.VISIBLE;
import static com.wiley.autotest.selenium.elements.upgrade.TeasyElementWrapper.wrap;
import static com.wiley.autotest.selenium.elements.upgrade.TeasyElementWrapper.wrapList;

/**
 * Finds different kinds of elements for example
 * visible, present in dom, alerts etc.
 */
public class TeasyElementFinder {

    private TeasyElement context;
    private TeasyFluentWait<WebDriver> fluentWait;
    private FramesConditionFactory conditionFactory;
    private SearchStrategy strategy;

    public TeasyElementFinder(WebDriver driver, SearchStrategy strategy) {
        this.fluentWait = new TeasyFluentWait<>(driver, strategy);
        this.strategy = strategy;
        this.conditionFactory = new FramesConditionFactory(strategy.getFrameStrategy());
    }

    public TeasyElementFinder(WebDriver driver, SearchStrategy strategy, TeasyElement context) {
        this.fluentWait = new TeasyFluentWait<>(driver, strategy);
        this.strategy = strategy;
        this.context = context;
        this.conditionFactory = new FramesConditionFactory(context, strategy.getFrameStrategy());
    }

    public TeasyElement visibleElement(By locator) {
        return getElement(locator, conditionFactory.get().visibility(locator), VISIBLE);
    }

    /**
     * note: Will return empty list in case no visible elements found
     */
    public List<TeasyElement> visibleElements(By locator) {
        return getElements(locator, conditionFactory.get().visibilities(locator), VISIBLE);
    }

    public TeasyElement presentInDomElement(By locator) {
        return getElement(locator, conditionFactory.get().presence(locator), DOM);
    }

    /**
     * note: Will return empty list in case no elements found in DOM
     */
    public List<TeasyElement> presentInDomElements(By locator) {
        return getElements(locator, conditionFactory.get().presences(locator), DOM);
    }

    //this is not "Teasy" alert yet, so should probably become "Teasy"
    public Alert alert() {
        return fluentWait.waitFor(ExpectedConditions.alertIsPresent());
    }

    private <T extends TeasyElement> List<T> getElements(By locator, Function<WebDriver, List<WebElement>> condition, TeasyElementType type) {
        List<WebElement> webElements;
        try {
            webElements = fluentWait.waitFor(condition);
        } catch (AssertionError ignoredToReturnEmptyList) {
            return new ArrayList<>();
        }
        if (webElements == null) {
            return new ArrayList<>();
        }
        if (context == null) {
            return wrapList(webElements, locator, type);
        } else {
            return wrapList(context, webElements, locator, type);
        }
    }

    private TeasyElement getElement(By locator, Function<WebDriver, WebElement> condition, TeasyElementType type) {
        WebElement webElement;
        try {
            webElement = fluentWait.waitFor(condition);
        } catch (AssertionError ignoredToReturnEmptyList) {
            webElement = null;
        }
        if (webElement == null) {

            //this is needed to return null when the user explicitly set to receive null in case of failure
            if (strategy.isNullOnFailure()) {
                return null;
            }

            if (context == null) {
                return wrap(null, locator, TeasyElementType.NULL);
            } else {
                return wrap(context, null, locator, TeasyElementType.NULL);
            }
        }
        if (context == null) {
            return wrap(webElement, locator, type);
        } else {
            return wrap(context, webElement, locator, type);
        }
    }
}
