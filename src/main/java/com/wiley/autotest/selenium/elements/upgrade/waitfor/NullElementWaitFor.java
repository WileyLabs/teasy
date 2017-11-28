package com.wiley.autotest.selenium.elements.upgrade.waitfor;

import com.wiley.autotest.selenium.elements.upgrade.NullTeasyElement;
import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import com.wiley.autotest.selenium.elements.upgrade.TeasyElementData;
import com.wiley.autotest.selenium.elements.upgrade.TeasyElementFinder;
import com.wiley.autotest.selenium.elements.upgrade.TeasyFluentWait;
import com.wiley.autotest.selenium.elements.upgrade.conditions.element.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import java.util.function.Function;

/**
 * Condition waiter for Null Element
 */
public class NullElementWaitFor implements ElementWaitFor {

    private final TeasyFluentWait<WebDriver> fluentWait;
    private final TeasyElementData elementData;
    private final TeasyElementFinder finder;

    public NullElementWaitFor(TeasyElementData elementData, TeasyFluentWait<WebDriver> fluentWait, TeasyElementFinder finder) {
        this.elementData = elementData;
        this.fluentWait = fluentWait;
        this.finder = finder;
    }

    public void displayed() {
        fluentWait.waitFor(new ElementDisplayed(getElement()));
    }

    public void absent() {
        doNothing();
    }

    public void text(String text) {
        fluentWait.waitFor(new ElementTextEquals(getElement(), text));
    }

    public void attribute(String attributeName, String value) {
        fluentWait.waitFor(new ElementAttributeValue(getElement(), attributeName, value));
    }

    public void attribute(String attributeName) {
        fluentWait.waitFor(new ElementHasAttribute(getElement(), attributeName));
    }

    public void notContainsAttributeValue(String attributeName, String value) {
        doNothing();
    }

    public void containsAttributeValue(String attributeName, String value) {
        fluentWait.waitFor(new ElementAttributeContain(getElement(), attributeName, value));
    }

    public void stale() {
        fluentWait.waitFor(new ElementStale(getElement()));
    }

    public void clickable() {
        fluentWait.waitFor(new ElementClickable(getElement()));
    }

    public void condition(Function<? super WebDriver, ?> condition) {
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
