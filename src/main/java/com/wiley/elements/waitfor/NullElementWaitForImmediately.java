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

    @Override
    public void displayed() {
        throwException();
    }

    @Override
    public void absent() {
        doNothing();
    }

    @Override
    public void text(String text) {
        throwException();
    }

    @Override
    public void attribute(String attributeName, String value) {
        throwException();
    }

    @Override
    public void attribute(String attributeName) {
        throwException();
    }

    @Override
    public void notContainsAttributeValue(String attributeName, String value) {
        throwException();
    }

    @Override
    public void containsAttributeValue(String attributeName, String value) {
        throwException();
    }

    @Override
    public void stale() {
        throwException();
    }

    @Override
    public void clickable() {
        throwException();
    }

    @Override
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
