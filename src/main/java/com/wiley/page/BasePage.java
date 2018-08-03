package com.wiley.page;

import com.wiley.assertions.SoftAssert;
import com.wiley.elements.*;
import com.wiley.elements.find.*;
import com.wiley.elements.types.TeasyElementList;
import com.wiley.elements.waitfor.CustomWaitFor;
import com.wiley.holders.AssertionsHolder;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import static com.wiley.holders.DriverHolder.getDriver;

/**
 * User: ntyukavkin
 * Date: 10.04.2018
 * Time: 14:57
 */
public class BasePage {

    protected WebDriver driver;

    public final void open(final String url) {
        if (!url.isEmpty()) {
            driver.get(url);
        }
    }

    public void init(WebDriver driver) {
        this.driver = driver;
        initFindByAnnotations(this);
        init();
    }

    protected void init() {
    }

    protected void handleRedirect() {
    }

    private <E extends BasePage> void initFindByAnnotations(final E page) {
        PageFactory.initElements(driver, page);
    }

    protected <E extends BasePage> E redirectTo(final Class<E> target) {
        final E page = PageProvider.get(target);
        page.handleRedirect();
        return page;
    }

    protected SoftAssert softAssert() {
        return AssertionsHolder.softAssert();
    }

    public CustomWaitFor waitFor() {
        return waitFor(new SearchStrategy());
    }

    public CustomWaitFor waitFor(SearchStrategy strategy) {
        return new CustomWaitFor(strategy);
    }

    public TeasyElement element(final By locator) {
        return element(locator, new SearchStrategy());
    }

    public TeasyElement element(final By locator, SearchStrategy strategy) {
        return new VisibleElementLookUp(getDriver(), strategy).find(locator);
    }

    public TeasyElementList elements(final By locator) {
        return elements(locator, new SearchStrategy());
    }

    public TeasyElementList elements(final By locator, SearchStrategy strategy) {
        return new VisibleElementsLookUp(getDriver(), strategy).find(locator);
    }

    public TeasyElement domElement(By locator) {
        return domElement(locator, new SearchStrategy());
    }

    public TeasyElement domElement(By locator, SearchStrategy strategy) {
        return new DomElementLookUp(getDriver(), strategy).find(locator);
    }

    public TeasyElementList domElements(By locator) {
        return domElements(locator, new SearchStrategy());
    }

    public TeasyElementList domElements(By locator, SearchStrategy strategy) {
        return new DomElementsLookUp(getDriver(), strategy).find(locator);
    }

    public Alert alert() {
        return alert(new SearchStrategy());
    }

    public Alert alert(SearchStrategy strategy) {
        return new AlertLookUp(driver, strategy).find();
    }

    public Window window() {
        return new TeasyWindow(driver);
    }
}
