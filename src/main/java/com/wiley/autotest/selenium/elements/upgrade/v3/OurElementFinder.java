package com.wiley.autotest.selenium.elements.upgrade.v3;

import com.wiley.autotest.selenium.context.OurSearchStrategy;
import com.wiley.autotest.selenium.elements.upgrade.OurWebElement;
import com.wiley.autotest.selenium.elements.upgrade.v3.conditions.OurConditionFactory;
import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.wiley.autotest.selenium.elements.upgrade.OurWebElementFactory.wrap;
import static com.wiley.autotest.selenium.elements.upgrade.OurWebElementFactory.wrapList;


/**
 * Finds different kinds of elements for example
 * visible, present in dom, alerts etc.
 */
public class OurElementFinder {

    private OurSearchStrategy strategy;

    private OurWebElement context;

    private WebDriverFluentWait fluentWait;

    public OurElementFinder(WebDriver driver, OurSearchStrategy strategy) {
        this.strategy = strategy;
        this.fluentWait = new WebDriverFluentWait(driver);
        fluentWait.withTimeout(strategy.getCustomTimeout(), TimeUnit.SECONDS);
        fluentWait.pollingEvery(strategy.getPoolingEvery(), strategy.getUnit());
        fluentWait.setNullOnFailure(strategy.isNullOnFailure());
    }

    public OurElementFinder(WebDriver driver, OurSearchStrategy strategy, OurWebElement context) {
        this(driver, strategy);
        this.context = context;
    }

    public OurWebElement visibleElement(By locator) {
        Function<WebDriver, List<WebElement>> condition = new OurConditionFactory(context).get(strategy.getFrameStrategy())
                .visibility(locator);
        return getElement(locator, condition);
    }

    /**
     * note: Will return empty list in case no visible elements found
     */
    public List<OurWebElement> visibleElements(By locator) {
        Function<WebDriver, List<WebElement>> condition = new OurConditionFactory(context).get(strategy.getFrameStrategy())
                .visibility(locator);
        return getElements(locator, condition);
    }

    public OurWebElement presentInDomElement(By locator) {
        Function<WebDriver, List<WebElement>> condition = new OurConditionFactory(context).get(strategy.getFrameStrategy())
                .presence(locator);
        return getElement(locator, condition);
    }

    /**
     * note: Will return empty list in case no visible elements found
     */
    public List<OurWebElement> presentInDomElements(By locator) {
        Function<WebDriver, List<WebElement>> condition = new OurConditionFactory(context).get(strategy.getFrameStrategy())
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

    private List<OurWebElement> getElements(By locator, Function<WebDriver, List<WebElement>> condition) {
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
    private OurWebElement getElement(By locator, Function<WebDriver, List<WebElement>> condition) {
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
