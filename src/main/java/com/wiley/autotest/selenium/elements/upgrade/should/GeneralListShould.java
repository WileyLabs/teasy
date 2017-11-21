package com.wiley.autotest.selenium.elements.upgrade.should;

import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import com.wiley.autotest.selenium.elements.upgrade.TeasyFluentWait;
import com.wiley.autotest.selenium.elements.upgrade.conditions.elements.ElementsDisplayed;
import com.wiley.autotest.selenium.elements.upgrade.conditions.elements.ElementsHaveTexts;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.function.Function;

public class GeneralListShould implements ListShould {

    private final TeasyFluentWait<WebDriver> fluentWait;
    private final List<TeasyElement> elements;

    public GeneralListShould(List<TeasyElement> elements, TeasyFluentWait<WebDriver> fluentWait) {
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

    }

    @Override
    public void haveTexts(List<String> texts) {
        waitFor(new ElementsHaveTexts(elements, texts));
    }

    @Override
    public void haveAttribute(String attributeName, String value) {

    }

    @Override
    public void haveAttribute(String attributeName) {

    }

    @Override
    public void notHaveAttribute(String attributeName) {

    }

    @Override
    public void customCondition(Function<WebDriver, Boolean> condition) {

    }

    private void waitFor(Function<WebDriver, Boolean> condition) {
        fluentWait.waitFor(condition);
    }
}
