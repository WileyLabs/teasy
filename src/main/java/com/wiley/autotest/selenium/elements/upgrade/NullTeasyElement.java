package com.wiley.autotest.selenium.elements.upgrade;

import com.wiley.autotest.selenium.context.SearchStrategy;
import com.wiley.autotest.selenium.elements.upgrade.v3.*;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;

import java.util.List;


/**
 * Represents element that is absent (not found)
 */
public class NullTeasyElement implements TeasyElement, Locatable {

    private TeasyElementData elementData;

    public NullTeasyElement(TeasyElementData elementData) {
        this.elementData = elementData;
    }

    @Override
    public Should should() {
        return new NullShouldImmediately(this);
    }

    @Override
    public Should should(SearchStrategy strategy) {
        return new NullShould(elementData, strategy);
    }

    @Override
    public ElementWaitFor waitFor() {
        return new NullElementWaitForImmediately(this);
    }

    @Override
    public ElementWaitFor waitFor(SearchStrategy strategy) {
        return new NullElementWaitFor(elementData, strategy);
    }

    /*
      All other methods of TeasyElement should throw an exception because it's not possible to
      interact with element that does not exist
     */

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) {
        throwException();
        return null;
    }

    @Override
    public WebElement getWrappedWebElement() {
        throwException();
        return null;
    }

    @Override
    public void submit() {
        throwException();
    }

    @Override
    public void sendKeys(CharSequence... charSequences) {
        throwException();
    }

    @Override
    public void clear() {
        throwException();
    }

    @Override
    public String getTagName() {
        throwException();
        return null;
    }

    @Override
    public String getAttribute(String s) {
        throwException();
        return null;
    }

    @Override
    public boolean isSelected() {
        throwException();
        return false;
    }

    @Override
    public boolean isEnabled() {
        throwException();
        return false;
    }

    @Override
    public boolean isStale() {
        throwException();
        return false;
    }

    @Override
    public String getText() {
        throwException();
        return null;
    }

    @Override
    public List<WebElement> findElements(By by) {
        throwException();
        return null;
    }

    @Override
    public WebElement findElement(By by) {
        throwException();
        return null;
    }

    @Override
    public boolean isDisplayed() {
        throwException();
        return false;
    }

    @Override
    public Point getLocation() {
        throwException();
        return null;
    }

    @Override
    public Dimension getSize() {
        throwException();
        return null;
    }

    @Override
    public Rectangle getRect() {
        throwException();
        return null;
    }

    @Override
    public String getCssValue(String s) {
        throwException();
        return null;
    }

    @Override
    public Locator getLocator() {
        throwException();
        return null;
    }


    @Override
    public TeasyElement getParent() {
        throwException();
        return null;
    }

    @Override
    public TeasyElement getParent(int level) {
        throwException();
        return null;
    }

    @Override
    public TeasyElement element(By by) {
        throwException();
        return null;
    }

    @Override
    public TeasyElement element(By by, SearchStrategy strategy) {
        throwException();
        return null;
    }

    @Override
    public List<TeasyElement> elements(By by) {
        throwException();
        return null;
    }

    @Override
    public List<TeasyElement> elements(By by, SearchStrategy strategy) {
        throwException();
        return null;
    }

    @Override
    public TeasyElement domElement(By by) {
        throwException();
        return null;
    }

    @Override
    public TeasyElement domElement(By by, SearchStrategy strategy) {
        throwException();
        return null;
    }

    @Override
    public List<TeasyElement> domElements(By by) {
        throwException();
        return null;
    }

    @Override
    public List<TeasyElement> domElements(By by, SearchStrategy strategy) {
        throwException();
        return null;
    }

    @Override
    public void click() {
        throwException();
    }

    @Override
    public Coordinates getCoordinates() {
        throwException();
        return null;
    }

    private void throwException() {
        throw new NoSuchElementException("Unable to find element with locator '" + elementData.getBy() + "'");
    }
}
