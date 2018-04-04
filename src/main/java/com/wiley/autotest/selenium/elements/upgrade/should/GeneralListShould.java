package com.wiley.autotest.selenium.elements.upgrade.should;

import com.wiley.autotest.selenium.elements.upgrade.TeasyElementList;
import com.wiley.autotest.selenium.elements.upgrade.TeasyFluentWait;
import com.wiley.autotest.selenium.elements.upgrade.conditions.elements.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.function.Function;

/**
 * Temp solution as a POC
 * <p>
 * most likely there will be VisibleListShould, DomListShould and EmptyListShould
 */
public class GeneralListShould implements ListShould {

    private final TeasyFluentWait<WebDriver> fluentWait;
    private final TeasyElementList elements;

    public GeneralListShould(TeasyElementList elements, TeasyFluentWait<WebDriver> fluentWait) {
        this.elements = elements;
        this.fluentWait = fluentWait;
    }

    @Override
    public void beDisplayed() {
        throwExceptionIfListIsEmpty();
        waitFor(new ElementsDisplayed(elements));
    }

    @Override
    public void beAbsent() {
        waitFor(new ElementsAbsent(elements));
    }

    @Override
    public void haveText(String text) {
        throwExceptionIfListIsEmpty();
        waitFor(new ElementsHaveText(elements, text));
    }

    @Override
    public void haveAnyText() {
        throwExceptionIfListIsEmpty();
        waitFor(new ElementsHaveAnyText(elements));
    }

    @Override
    public void haveTexts(List<String> texts) {
        throwExceptionIfListIsEmpty();
        waitFor(new ElementsHaveTexts(elements, texts));
    }

    @Override
    public void haveAttribute(String attributeName, String value) {
        throwExceptionIfListIsEmpty();
        waitFor(new ElementsAttributeValue(elements, attributeName, value));
    }

    @Override
    public void haveAttribute(String attributeName) {
        throwExceptionIfListIsEmpty();
        waitFor(new ElementsHaveAttribute(elements, attributeName));
    }

    @Override
    public void notHaveAttribute(String attributeName) {
        throwExceptionIfListIsEmpty();
        waitFor(new ElementsNotHaveAttribute(elements, attributeName));
    }

    @Override
    public void customCondition(Function<WebDriver, Boolean> condition) {
        throwExceptionIfListIsEmpty();
        waitFor(condition);
    }

    private void waitFor(Function<WebDriver, Boolean> condition) {
        fluentWait.waitFor(condition);
    }

    private void throwExceptionIfListIsEmpty() {
        if (elements.isEmpty()) {
            throw new NoSuchElementException("Unable to find elements");
        }
    }
}
