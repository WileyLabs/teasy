package com.wiley.elements.waitfor;

import com.wiley.elements.SearchStrategy;
import com.wiley.elements.TeasyElement;
import com.wiley.elements.TeasyElementData;
import com.wiley.elements.TeasyFluentWait;
import com.wiley.elements.conditions.element.*;
import com.wiley.elements.find.DomElementLookUp;
import com.wiley.elements.types.NullTeasyElement;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import java.util.function.Function;

import static com.wiley.holders.DriverHolder.getDriver;

/**
 * Condition waiter for {@link NullTeasyElement}
 */
public class NullElementWaitFor implements ElementWaitFor {

    private final TeasyFluentWait<WebDriver> wait;
    private final TeasyElementData elementData;
    private final SearchStrategy strategy;

    public NullElementWaitFor(TeasyElementData elementData, TeasyFluentWait<WebDriver> wait,
                              SearchStrategy strategy) {
        this.elementData = elementData;
        this.wait = wait;
        this.strategy = strategy;
    }

    public void displayed() {
        wait.waitFor(new ElementDisplayed(getElement()));
    }

    public void absent() {
        doNothing();
    }

    public void text(String text) {
        wait.waitFor(new ElementHasText(getElement(), text));
    }

    public void attribute(String attributeName, String value) {
        wait.waitFor(new ElementAttributeValue(getElement(), attributeName, value));
    }

    public void attribute(String attributeName) {
        wait.waitFor(new ElementHasAttribute(getElement(), attributeName));
    }

    public void notContainsAttributeValue(String attributeName, String value) {
        doNothing();
    }

    public void containsAttributeValue(String attributeName, String value) {
        wait.waitFor(new ElementAttributeContain(getElement(), attributeName, value));
    }

    public void stale() {
        wait.waitFor(new ElementStale(getElement()));
    }

    public void clickable() {
        wait.waitFor(new ElementClickable(getElement()));
    }

    public void condition(Function<? super WebDriver, ?> condition) {
        wait.waitFor(condition);
    }

    private void throwException() {
        throw new NoSuchElementException("Unable to find element with locator '" + elementData.getBy() + "'");
    }

    private TeasyElement getElement() {
        TeasyElement lastAttemptToGetElement = new DomElementLookUp(getDriver(), strategy,
                elementData.getSearchContext()).find(elementData.getBy());
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
