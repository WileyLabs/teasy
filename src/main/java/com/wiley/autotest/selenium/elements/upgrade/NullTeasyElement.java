package com.wiley.autotest.selenium.elements.upgrade;

import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.selenium.context.SearchStrategy;
import com.wiley.autotest.selenium.elements.upgrade.should.NullShould;
import com.wiley.autotest.selenium.elements.upgrade.should.NullShouldImmediately;
import com.wiley.autotest.selenium.elements.upgrade.should.Should;
import com.wiley.autotest.selenium.elements.upgrade.waitfor.ElementWaitFor;
import com.wiley.autotest.selenium.elements.upgrade.waitfor.NullElementWaitFor;
import com.wiley.autotest.selenium.elements.upgrade.waitfor.NullElementWaitForImmediately;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;

import java.util.List;

import static com.wiley.autotest.selenium.SeleniumHolder.getWebDriver;

/**
 * Represents element that is absent (not found)
 */
public class NullTeasyElement implements TeasyElement, Locatable {

    private TeasyElementData elementData;
    private Locator locator;

    public NullTeasyElement(TeasyElementData elementData) {
        this.elementData = elementData;
        this.locator = new ElementLocatorFactory(elementData, getWebDriver()).getLocator();
    }

    @Override
    public Should should() {
        return new NullShouldImmediately(this);
    }

    @Override
    public Should should(SearchStrategy strategy) {
        TeasyElementFinder finder;
        if (elementData.getSearchContext() == null) {
            finder = new TeasyElementFinder(SeleniumHolder.getWebDriver(), strategy);
        } else {
            finder = new TeasyElementFinder(SeleniumHolder.getWebDriver(), strategy, elementData.getSearchContext());
        }
        return new NullShould(elementData, new TeasyFluentWait<>(getWebDriver(), strategy), finder);
    }

    @Override
    public ElementWaitFor waitFor() {
        return new NullElementWaitForImmediately(this);
    }

    @Override
    public ElementWaitFor waitFor(SearchStrategy strategy) {
        TeasyElementFinder finder;
        if (elementData.getSearchContext() == null) {
            finder = new TeasyElementFinder(SeleniumHolder.getWebDriver(), strategy);
        } else {
            finder = new TeasyElementFinder(SeleniumHolder.getWebDriver(), strategy, elementData.getSearchContext());
        }
        return new NullElementWaitFor(elementData, new TeasyFluentWait<>(getWebDriver(), strategy), finder);
    }

    /*
      All other methods of TeasyElement should throw an exception because it's not possible to
      interact with element that does not exist
     */

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) {
        throw noSuchElementException();
    }

    @Override
    public WebElement getWrappedWebElement() {
        throw noSuchElementException();
    }

    @Override
    public void submit() {
        throw noSuchElementException();
    }

    @Override
    public void sendKeys(CharSequence... charSequences) {
        throw noSuchElementException();
    }

    @Override
    public void clear() {
        throw noSuchElementException();
    }

    @Override
    public String getTagName() {
        throw noSuchElementException();
    }

    @Override
    public String getAttribute(String s) {
        throw noSuchElementException();
    }

    @Override
    public boolean isSelected() {
        throw noSuchElementException();
    }

    @Override
    public boolean isEnabled() {
        throw noSuchElementException();
    }

    @Override
    public boolean isStale() {
        throw noSuchElementException();
    }

    @Override
    public String getText() {
        throw noSuchElementException();
    }

    @Override
    public List<WebElement> findElements(By by) {
        throw noSuchElementException();
    }

    @Override
    public WebElement findElement(By by) {
        throw noSuchElementException();
    }

    @Override
    public boolean isDisplayed() {
        throw noSuchElementException();
    }

    @Override
    public Point getLocation() {
        throw noSuchElementException();
    }

    @Override
    public Dimension getSize() {
        throw noSuchElementException();
    }

    @Override
    public Rectangle getRect() {
        throw noSuchElementException();
    }

    @Override
    public String getCssValue(String s) {
        throw noSuchElementException();
    }

    @Override
    public Locator getLocator() {
        return locator;
    }

    @Override
    public TeasyElement getParent() {
        throw noSuchElementException();
    }

    @Override
    public TeasyElement getParent(int level) {
        throw noSuchElementException();
    }

    @Override
    public TeasyElement element(By by) {
        throw noSuchElementException();
    }

    @Override
    public TeasyElement element(By by, SearchStrategy strategy) {
        throw noSuchElementException();
    }

    @Override
    public TeasyElementList elements(By by) {
        throw noSuchElementException();
    }

    @Override
    public TeasyElementList elements(By by, SearchStrategy strategy) {
        throw noSuchElementException();
    }

    @Override
    public TeasyElement domElement(By by) {
        throw noSuchElementException();
    }

    @Override
    public TeasyElement domElement(By by, SearchStrategy strategy) {
        throw noSuchElementException();
    }

    @Override
    public TeasyElementList domElements(By by) {
        throw noSuchElementException();
    }

    @Override
    public TeasyElementList domElements(By by, SearchStrategy strategy) {
        throw noSuchElementException();
    }

    @Override
    public void click() {
        throw noSuchElementException();
    }

    @Override
    public Coordinates getCoordinates() {
        throw noSuchElementException();
    }

    @NotNull
    private NoSuchElementException noSuchElementException() {
        return new NoSuchElementException("Unable to find element with locator '" + elementData.getBy() + "'");
    }
}
