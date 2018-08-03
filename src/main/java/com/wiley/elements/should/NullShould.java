package com.wiley.elements.should;

import com.wiley.elements.SearchStrategy;
import com.wiley.elements.TeasyElement;
import com.wiley.elements.TeasyElementData;
import com.wiley.elements.TeasyFluentWait;
import com.wiley.elements.conditions.element.*;
import com.wiley.elements.find.DomElementLookUp;
import com.wiley.elements.types.NullTeasyElement;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import java.util.function.Function;

import static com.wiley.holders.DriverHolder.getDriver;

/**
 * Assertions for Null Element
 */
public class NullShould implements Should {

    private final TeasyFluentWait<WebDriver> fluentWait;
    private final TeasyElementData elementData;
    private final SearchStrategy strategy;

    public NullShould(TeasyElementData elementData, TeasyFluentWait<WebDriver> fluentWait,
                      SearchStrategy strategy) {
        this.elementData = elementData;
        this.fluentWait = fluentWait;
        this.strategy = strategy;
    }

    public void beDisplayed() {
        fluentWait.waitFor(new ElementDisplayed(getElement()));
    }

    public void beAbsent() {
        doNothing();
    }

    public void haveText(String text) {
        fluentWait.waitFor(new ElementHasText(getElement(), text));
    }

    public void haveNoText() {
        fluentWait.waitFor(new ElementHasNoText(getElement()));
    }

    public void containsText(String text) {
        fluentWait.waitFor(new ElementContainsText(getElement(), text));
    }

    public void haveAnyText() {
        fluentWait.waitFor(new ElementDisplayed(getElement()));
    }

    public void haveAttribute(String attributeName, String value) {
        fluentWait.waitFor(new ElementAttributeValue(getElement(), attributeName, value));
    }

    public void haveAttribute(String attributeName) {
        fluentWait.waitFor(new ElementHasAttribute(getElement(), attributeName));
    }

    public void notHaveAttribute(String attributeName) {
        fluentWait.waitFor(new ElementNotHaveAttribute(getElement(), attributeName));
    }

    public void customCondition(Function<WebDriver, Boolean> condition) {
        fluentWait.waitFor(condition);
    }

    private void throwException() {
        throw new NoSuchElementException("Unable to find element with locator '" + elementData.getBy() + "'");
    }

    private TeasyElement getElement() {
        TeasyElement lastAttemptToGetElement = new DomElementLookUp(getDriver(), strategy,
                elementData.getSearchContext()).find(elementData.getBy());
        if (lastAttemptToGetElement instanceof NullTeasyElement) {
            //if element is not found again - throw exception
            throwException();
            return null;
        }

        return lastAttemptToGetElement;
    }

    //We can log something here... or just keep it as empty(delete)
    private void doNothing() {
    }
}