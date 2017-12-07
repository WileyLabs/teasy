package com.wiley.autotest.selenium.elements.upgrade.should;

import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import com.wiley.autotest.selenium.elements.upgrade.TeasyElementList;
import com.wiley.autotest.selenium.elements.upgrade.TeasyFluentWait;
import com.wiley.autotest.selenium.elements.upgrade.conditions.elements.*;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.function.Function;

/**
 * Temp solution as a POC
 *
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

    }

    @Override
    public void haveText(String text) {
        waitFor(new ElementsHaveText(elements, text));
    }

    @Override
    public void haveTexts(List<String> texts) {
        waitFor(new ElementsHaveTexts(elements, texts));
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
