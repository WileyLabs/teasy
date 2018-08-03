package com.wiley.elements.should;

import com.wiley.elements.TeasyFluentWait;
import com.wiley.elements.conditions.elements.*;
import com.wiley.elements.types.TeasyElementList;
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
        waitFor(new ElementsDisplayed(elements));
    }

    @Override
    public void beAbsent() {
        waitFor(new ElementsAbsent(elements));
    }

    @Override
    public void haveSize(int expectedSize) {
        waitFor(new ElementsHaveSize(elements, expectedSize));
    }

    @Override
    public void haveText(String text) {
        waitFor(new ElementsHaveText(elements, text));
    }

    @Override
    public void haveAnyText() {
        waitFor(new ElementsHaveAnyText(elements));
    }

    @Override
    public void haveTexts(List<String> texts) {
        waitFor(new ElementsHaveTexts(elements, texts));
    }

    @Override
    public void haveNoText() {
        waitFor(new ElementsHaveNoText(elements));
    }

    @Override
    public void containsText(String text) {
        waitFor(new ElementsContainsText(elements, text));
    }

    @Override
    public void haveAttribute(String attributeName, String value) {
        waitFor(new ElementsAttributeValue(elements, attributeName, value));
    }

    @Override
    public void haveAttribute(String attributeName) {
        waitFor(new ElementsHaveAttribute(elements, attributeName));
    }

    @Override
    public void notHaveAttribute(String attributeName) {
        waitFor(new ElementsNotHaveAttribute(elements, attributeName));
    }

    @Override
    public void customCondition(Function<WebDriver, Boolean> condition) {
        waitFor(condition);
    }

    private void waitFor(Function<WebDriver, Boolean> condition) {
        fluentWait.waitFor(condition);
    }
}
