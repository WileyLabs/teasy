package com.wiley.autotest.selenium.elements.upgrade.v3;

import com.wiley.autotest.ExpectedConditions2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.wiley.autotest.selenium.elements.upgrade.OurWebElementFactory.wrap;
import static com.wiley.autotest.selenium.elements.upgrade.OurWebElementFactory.wrapList;

;

/**
 * Created by vefimov on 03/05/2017.
 */
public class OurElementFinder {

    private SearchStrategy strategy;

    private FluentWaitFinder fluentWait;

    public OurElementFinder(WebDriver driver) {
        this.fluentWait = new FluentWaitFinder(driver);
    }

    public OurElementFinder(WebDriver driver, SearchStrategy strategy) {
        this(driver);
        this.strategy = strategy;
        fluentWait.withTimeout(strategy.getTimeout(), TimeUnit.SECONDS);
        fluentWait.pollingEvery(strategy.getPoolingEvery(), strategy.getUnit());
    }

    public WebElement visibleElement(By locator) {
        //todo name of condition should be different (currently we return as soon as there is at least 1 visible element but not ALL)
        return wrap(fluentWait.waitFor(ExpectedConditions2.visibilityOfAllElementsLocatedBy(locator)).get(0), locator);
    }

    public List<WebElement> visibleElements(By locator) {
        //todo name of condition should be different (currently we return as soon as there is at least 1 visible element but not ALL)
        switch (strategy.getFrameStrategy()) {
            case FIRST_ELEMENTS:
                return wrapList(fluentWait.waitFor(ExpectedConditions2.visibilityOfAllElementsLocatedBy(locator)), locator);
            case IN_ALL_FRAMES:
                return wrapList(fluentWait.waitFor(ExpectedConditions2.visibilityOfAllElementsLocatedByInFrames(locator)), locator);
        }
        throw new EnumConstantNotPresentException(strategy.getFrameStrategy()
                .getDeclaringClass(), " enum constant is not recognized. Select from FIRST_ELEMENTS or IN_ALL_FRAMES.");
    }

    public WebElement presentInDomElement(By locator) {
        return wrap(fluentWait.waitFor(ExpectedConditions.presenceOfElementLocated(locator)), locator);
    }

    public List<WebElement> presentInDomElements(By locator) {
        switch (strategy.getFrameStrategy()) {
            case FIRST_ELEMENTS:
                return wrapList(fluentWait.waitFor(ExpectedConditions.presenceOfAllElementsLocatedBy(locator)), locator);
            case IN_ALL_FRAMES:
                return wrapList(fluentWait.waitFor(ExpectedConditions2.presenceOfAllElementsLocatedByInFrames(locator)), locator);
        }
        throw new EnumConstantNotPresentException(strategy.getFrameStrategy()
                .getDeclaringClass(), " enum constant is not recognized. Select from FIRST_ELEMENTS or IN_ALL_FRAMES.");
    }

}
