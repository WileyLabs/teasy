package com.wiley.autotest.selenium.elements.upgrade.v3;

import com.wiley.autotest.selenium.elements.upgrade.v3.conditions.OurConditionFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.wiley.autotest.selenium.elements.upgrade.OurWebElementFactory.wrap;
import static com.wiley.autotest.selenium.elements.upgrade.OurWebElementFactory.wrapList;

;

/**
 * Created by vefimov on 03/05/2017.
 */
public class OurElementFinder {

    private OurSearchStrategy strategy;

    private SearchContext context;

    private FluentWaitFinder fluentWait;

    public OurElementFinder(WebDriver driver) {
        this.fluentWait = new FluentWaitFinder(driver);
    }

    public OurElementFinder(WebDriver driver, SearchContext context) {
        this.fluentWait = new FluentWaitFinder(driver);
        this.context = context;
    }

    public OurElementFinder(WebDriver driver, OurSearchStrategy strategy) {
        this(driver);
        this.strategy = strategy;
        fluentWait.withTimeout(strategy.getTimeout(), TimeUnit.SECONDS);
        fluentWait.pollingEvery(strategy.getPoolingEvery(), strategy.getUnit());
    }

    public OurElementFinder(WebDriver driver, SearchContext context, OurSearchStrategy strategy) {
        this(driver, context);
        this.strategy = strategy;
        fluentWait.withTimeout(strategy.getTimeout(), TimeUnit.SECONDS);
        fluentWait.pollingEvery(strategy.getPoolingEvery(), strategy.getUnit());
    }

    public WebElement visibleElement(By locator) {
        Function<WebDriver, List<WebElement>> condition = new OurConditionFactory(context).get(strategy.getFrameStrategy())
                .visibility(locator);
        return wrap(fluentWait.waitFor(condition).get(0), locator);
    }

    public List<WebElement> visibleElements(By locator) {
        Function<WebDriver, List<WebElement>> condition = new OurConditionFactory(context).get(strategy.getFrameStrategy())
                .visibility(locator);
        return wrapList(fluentWait.waitFor(condition), locator);
    }

    public WebElement presentInDomElement(By locator) {
        Function<WebDriver, List<WebElement>> condition = new OurConditionFactory(context).get(strategy.getFrameStrategy())
                .presence(locator);
        return wrap(fluentWait.waitFor(condition).get(0), locator);
    }

    public List<WebElement> presentInDomElements(By locator) {
        Function<WebDriver, List<WebElement>> condition = new OurConditionFactory(context).get(strategy.getFrameStrategy())
                .presence(locator);
        return wrapList(fluentWait.waitFor(condition), locator);

    }

}
