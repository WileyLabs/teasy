package com.wiley.elements.types;

import com.wiley.elements.*;
import com.wiley.elements.should.NullShould;
import com.wiley.elements.should.NullShouldImmediately;
import com.wiley.elements.should.Should;
import com.wiley.elements.types.locate.LocatableFactory;
import com.wiley.elements.waitfor.ElementWaitFor;
import com.wiley.elements.waitfor.NullElementWaitFor;
import com.wiley.elements.waitfor.NullElementWaitForImmediately;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.internal.Coordinates;

import java.util.List;

import static com.wiley.holders.DriverHolder.getDriver;

/**
 * Represents element that is absent (not found)
 */
public class NullTeasyElement implements TeasyElement, org.openqa.selenium.interactions.internal.Locatable {

    private TeasyElementData elementData;
    private Locatable locatable;

    public NullTeasyElement(TeasyElementData elementData) {
        this.elementData = elementData;
        this.locatable = new LocatableFactory(elementData, getDriver()).get();
    }

    @Override
    public Should should() {
        return new NullShouldImmediately(this);
    }

    @Override
    public Should should(SearchStrategy strategy) {
        return new NullShould(elementData, new TeasyFluentWait<>(getDriver(), strategy), strategy);
    }

    @Override
    public ElementWaitFor waitFor() {
        return new NullElementWaitForImmediately(this);
    }

    @Override
    public ElementWaitFor waitFor(SearchStrategy strategy) {
        return new NullElementWaitFor(elementData, new TeasyFluentWait<>(getDriver(), strategy), strategy);
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
    public Locatable getLocatable() {
        return locatable;
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

    private NoSuchElementException noSuchElementException() {
        return new NoSuchElementException("Unable to find element with locatable '" + elementData.getBy() + "'");
    }
}
