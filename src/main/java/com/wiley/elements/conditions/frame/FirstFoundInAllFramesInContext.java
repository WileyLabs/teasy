package com.wiley.elements.conditions.frame;

import com.wiley.elements.TeasyElement;
import com.wiley.elements.conditions.TeasyExpectedConditions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.function.Function;

/**
 * same as {@link FirstFoundInAllFrames} but searches within another element's context
 */
public class FirstFoundInAllFramesInContext implements ElementFrameCondition {

    private TeasyElement context;

    public FirstFoundInAllFramesInContext(TeasyElement context) {
        this.context = context;
    }

    @Override
    public Function<WebDriver, List<WebElement>> visibilityOfList(By locator) {
        return TeasyExpectedConditions.visibilityOfFirstElementsInAllFrames(context, locator);
    }

    @Override
    public Function<WebDriver, WebElement> visibility(By locator) {
        return TeasyExpectedConditions.visibilityOfFirstElementInAllFrames(context, locator);
    }

    @Override
    public Function<WebDriver, List<WebElement>> presenceOfList(By locator) {
        return TeasyExpectedConditions.presenceOfAllElementsInAllFrames(context, locator);
    }

    @Override
    public Function<WebDriver, WebElement> presence(By locator) {
        return TeasyExpectedConditions.presenceOfElementInAllFrames(context, locator);
    }
}
