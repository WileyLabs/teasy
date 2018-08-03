package com.wiley.elements.should;

import com.wiley.elements.TeasyFluentWait;
import com.wiley.elements.conditions.elements.ElementsAbsent;
import com.wiley.elements.types.TeasyElementList;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.function.Function;

/**
 * Assertions for an empty list (list with size == 0)
 * <p>
 * Pretty much for every case assertions will throw exception
 * as it does not make sense to check anything for an empty list
 * <p>
 * The exclusion is beAbsent() method - which will return "OK"
 * as if we want no elements to be present - empty list matches the condition
 */
public class EmptyListShould implements ListShould {

    private final TeasyFluentWait<WebDriver> fluentWait;
    private final TeasyElementList elements;

    public EmptyListShould(TeasyElementList elements, TeasyFluentWait<WebDriver> fluentWait) {
        this.elements = elements;
        this.fluentWait = fluentWait;
    }

    @Override
    public void beDisplayed() {
        throwException();
    }

    @Override
    public void beAbsent() {
        waitFor(new ElementsAbsent(elements));
    }

    @Override
    public void haveSize(int expectedSize) {
        throwException();
    }

    @Override
    public void haveText(String text) {
        throwException();
    }

    @Override
    public void haveAnyText() {
        throwException();
    }

    @Override
    public void haveTexts(List<String> texts) {
        throwException();
    }

    @Override
    public void haveNoText() {
        throwException();
    }

    @Override
    public void containsText(String text) {
        throwException();
    }

    @Override
    public void haveAttribute(String attributeName, String value) {
        throwException();
    }

    @Override
    public void haveAttribute(String attributeName) {
        throwException();
    }

    @Override
    public void notHaveAttribute(String attributeName) {
        throwException();
    }

    @Override
    public void customCondition(Function<WebDriver, Boolean> condition) {
        waitFor(condition);
    }

    private void waitFor(Function<WebDriver, Boolean> condition) {
        fluentWait.waitFor(condition);
    }

    private void throwException() {
        throw new NoSuchElementException("Didn't find any elements with locator '" + elements.getLocator() + "'!");
    }
}
