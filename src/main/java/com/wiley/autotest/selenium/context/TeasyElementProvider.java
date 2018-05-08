package com.wiley.autotest.selenium.context;

import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.selenium.elements.*;
import com.wiley.autotest.selenium.elements.upgrade.*;
import com.wiley.autotest.selenium.elements.upgrade.waitfor.CustomWaitFor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.wiley.autotest.selenium.SeleniumHolder.getWebDriver;

/**
 * Entry point to search for all elements on the screen
 */
public abstract class TeasyElementProvider {

    public static final Logger LOGGER = LoggerFactory.getLogger(TeasyElementProvider.class);

    //default finder that uses timeout from pom
    private TeasyElementFinder finder;

    protected TeasyElementFinder customFinder(SearchStrategy strategy) {
        return new TeasyElementFinder(getWebDriver(), strategy);
    }

    protected TeasyElementFinder finder() {
        if (finder == null) {
            finder = new TeasyElementFinder(getWebDriver(), new SearchStrategy());
        }
        return finder;
    }

    protected CustomWaitFor waitFor() {
        return new CustomWaitFor();
    }

    protected TeasyElement element(final By locator) {
        return finder().visibleElement(locator);
    }

    protected TeasyElement element(final By locator, SearchStrategy strategy) {
        return customFinder(strategy).visibleElement(locator);
    }

    protected TeasyElementList elements(final By locator) {
        return new TeasyElementList(finder().visibleElements(locator), locator);
    }

    protected TeasyElementList elements(final By locator, SearchStrategy strategy) {
        return new TeasyElementList(customFinder(strategy).visibleElements(locator), locator);
    }

    protected TeasyElement domElement(By locator) {
        return finder().presentInDomElement(locator);
    }

    protected TeasyElement domElement(By locator, SearchStrategy strategy) {
        return customFinder(strategy).presentInDomElement(locator);
    }

    protected TeasyElementList domElements(By locator) {
        return new TeasyElementList(finder().presentInDomElements(locator), locator);
    }

    protected TeasyElementList domElements(By locator, SearchStrategy strategy) {
        return new TeasyElementList(customFinder(strategy).presentInDomElements(locator), locator);
    }

    protected Alert alert() {
        return finder().alert();
    }

    protected Alert alert(SearchStrategy strategy) {
        return customFinder(strategy).alert();
    }

    protected Window window() {
        return new TeasyWindow(SeleniumHolder.getWebDriver());
    }

}

