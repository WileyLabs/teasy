package com.wiley.autotest.selenium.elements.upgrade.waitfor;

import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import com.wiley.autotest.selenium.elements.upgrade.TeasyFluentWait;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import java.util.function.Function;

/**
 * Created by vefimov on 26/04/2017.
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
        new TeasyFluentWait<>(SeleniumHolder.getWebDriver()).waitFor(condition);
    }

    private void throwException() {
        throw new NoSuchElementException("Unable to find element with locator '" + element.getLocator().getBy() + "'");
    }

    //We can log something here... or just keep it as empty(delete)
    private void doNothing() {
    }
}
