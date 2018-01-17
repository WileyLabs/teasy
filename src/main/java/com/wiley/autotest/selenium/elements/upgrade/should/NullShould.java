package com.wiley.autotest.selenium.elements.upgrade.should;

import com.wiley.autotest.selenium.elements.upgrade.*;
import com.wiley.autotest.selenium.elements.upgrade.conditions.element.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import java.util.function.Function;

/**
 * Assertions for Null Element
 */
public class NullShould implements Should {

    private final TeasyFluentWait<WebDriver> fluentWait;
    private final TeasyElementData elementData;
    private final TeasyElementFinder finder;

    public NullShould(TeasyElementData elementData, TeasyFluentWait<WebDriver> fluentWait, TeasyElementFinder finder) {
        this.elementData = elementData;
        this.fluentWait = fluentWait;
        this.finder = finder;
    }

    public void beDisplayed() {
        fluentWait.waitFor(new ElementDisplayed(getElement()));
    }

    public void beAbsent() {
        doNothing();
    }

    public void haveText(String text) {
        fluentWait.waitFor(new ElementTextEquals(getElement(), text));
    }

    public void haveAnyText() {
        fluentWait.waitFor(new ElementDisplayed(getElement()));
    }

    public void haveAttribute(String attributeName, String value) {
        fluentWait.waitFor(new ElementAttributeValue(getElement(), attributeName, value));
    }

    public void haveAttribute(String attributeName) {
        fluentWait.waitFor(new ElementHasAttribute(getElement(), attributeName));
    }

    public void notHaveAttribute(String attributeName) {
        fluentWait.waitFor(new ElementNotHaveAttribute(getElement(), attributeName));
    }

    public void customCondition(Function<WebDriver, Boolean> condition) {
        fluentWait.waitFor(condition);
    }

    private void throwException() {
        throw new NoSuchElementException("Unable to find element with locator '" + elementData.getBy() + "'");
    }

    private TeasyElement getElement() {
        TeasyElement lastAttemptToGetElement = finder.presentInDomElement(elementData.getBy());
        if (lastAttemptToGetElement instanceof NullTeasyElement) {
            //if element is not found again - throw exception
            throwException();
            return null;
        }

        return lastAttemptToGetElement;
    }

    //We can log something here... or just keep it as empty(delete)
    private void doNothing() {
    }

}