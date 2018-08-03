package com.wiley.elements.waitfor;

import com.wiley.elements.SearchStrategy;
import com.wiley.elements.TeasyElement;
import com.wiley.elements.TeasyFluentWait;
import com.wiley.elements.types.NullTeasyElement;
import com.wiley.holders.DriverHolder;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import java.util.function.Function;

/**
 * Immediate Condition waiter for {@link NullTeasyElement}
 * Immediately checks for a condition
 * (does not wait for a default timeout as in {@link NullTeasyElement})
 */
public class NullElementWaitForImmediately implements ElementWaitFor {

    private final TeasyElement element;

    public NullElementWaitForImmediately(TeasyElement element) {
        this.element = element;
    }

    public void displayed() {
        throwException();
    }

    public void absent() {
        doNothing();
    }

    public void text(String text) {
        throwException();
    }

    public void attribute(String attributeName, String value) {
        throwException();
    }

    public void attribute(String attributeName) {
        throwException();
    }

    public void notContainsAttributeValue(String attributeName, String value) {
        throwException();
    }

    public void containsAttributeValue(String attributeName, String value) {
        throwException();
    }

    public void stale() {
        throwException();
    }

    public void clickable() {
        throwException();
    }

    public void condition(Function<? super WebDriver, ?> condition) {
        new TeasyFluentWait<>(DriverHolder.getDriver(), new SearchStrategy()).waitFor(condition);
    }

    private void throwException() {
        throw new NoSuchElementException("Unable to find element with locator '" + element.getLocatable()
                .getBy() + "'");
    }

    //We can log something here... or just keep it as empty(delete)
    private void doNothing() {
    }
}
