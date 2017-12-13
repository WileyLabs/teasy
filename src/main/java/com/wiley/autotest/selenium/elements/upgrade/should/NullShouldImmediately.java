package com.wiley.autotest.selenium.elements.upgrade.should;

import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.selenium.context.SearchStrategy;
import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import com.wiley.autotest.selenium.elements.upgrade.TeasyFluentWait;
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
        throwException();
    }

    //null element is absent by default
    public void beAbsent() {
        doNothing();
    }

    public void haveText(String text) {
        throwException();
    }

    public void haveText() { throwException(); }

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
        new TeasyFluentWait<>(SeleniumHolder.getWebDriver(), new SearchStrategy(0)).waitFor(condition);
    }

    private void throwException() {
        throw new NoSuchElementException("Unable to find element with locator '" + element.getLocator().getBy() + "'");
    }

    //We can log something here... or just keep it as empty(delete)
    private void doNothing() {
    }
}
