package com.wiley.elements.should;

import com.wiley.elements.*;
import com.wiley.elements.conditions.element.*;
import com.wiley.elements.find.DomElementLookUp;
import com.wiley.elements.types.NullTeasyElement;
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

    @Override
    public void beDisplayed() {
        fluentWait.waitFor(new ElementDisplayed(getElement()));
    }

    @Override
    public void beAbsent() {
        doNothing();
    }

    @Override
    public void haveText(String text) {
        fluentWait.waitFor(new ElementHasText(getElement(), text));
    }

    @Override
    public void haveNoText() {
        fluentWait.waitFor(new ElementHasNoText(getElement()));
    }

    @Override
    public void containsText(String text) {
        fluentWait.waitFor(new ElementContainsText(getElement(), text));
    }

    @Override
    public void haveAnyText() {
        fluentWait.waitFor(new ElementDisplayed(getElement()));
    }

    @Override
    public void haveAttribute(String attributeName, String value) {
        fluentWait.waitFor(new ElementAttributeValue(getElement(), attributeName, value));
    }

    @Override
    public void haveAttribute(String attributeName) {
        fluentWait.waitFor(new ElementHasAttribute(getElement(), attributeName));
    }

    @Override
    public void notHaveAttribute(String attributeName) {
        fluentWait.waitFor(new ElementNotHaveAttribute(getElement(), attributeName));
    }

    @Override
    public void customCondition(Function<WebDriver, Boolean> condition) {
        fluentWait.waitFor(condition);
    }

    private void throwException() {
        throw new NotFoundElException(elementData.getBy());
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