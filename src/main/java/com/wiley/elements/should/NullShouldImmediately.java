package com.wiley.elements.should;

import com.wiley.elements.SearchStrategy;
import com.wiley.elements.TeasyElement;
import com.wiley.elements.TeasyFluentWait;
import com.wiley.holders.DriverHolder;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import java.util.function.Function;

public class NullShouldImmediately implements Should {

    private final TeasyElement element;

    public NullShouldImmediately(TeasyElement element) {
        this.element = element;
    }

    //null element can't be displayed
    public void beDisplayed() {
        throw new AssertionError("Element is absent in DOM. Locatable is '" + element.getLocatable().getBy() + "'");
    }

    //null element is absent by default
    public void beAbsent() {
        doNothing();
    }

    public void haveText(String text) {
        throwException();
    }

    public void haveAnyText() {
        throwException();
    }

    public void haveNoText() {
        throwException();
    }

    public void containsText(String text) {
        throwException();
    }

    public void haveAttribute(String attributeName, String value) {
        throwException();
    }

    public void haveAttribute(String attributeName) {
        throwException();
    }

    public void notHaveAttribute(String attributeName) {
        throwException();
    }

    public void customCondition(Function<WebDriver, Boolean> condition) {
        new TeasyFluentWait<>(DriverHolder.getDriver(), new SearchStrategy(0)).waitFor(condition);
    }

    private void throwException() {
        throw new NoSuchElementException("Unable to find element with locator '" + element.getLocatable().getBy() + "'");
    }

    //We can log something here... or just keep it as empty(delete)
    private void doNothing() {
    }
}
