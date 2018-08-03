package com.wiley.elements.conditions.frame;

import com.wiley.elements.conditions.TeasyExpectedConditions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.function.Function;

/**
 * Conditions will perform search for element(s) in All frames
 */
public class FirstFoundInAllFrames implements ElementFrameCondition {

    @Override
    public Function<WebDriver, List<WebElement>> visibilityOfList(By locator) {
        return TeasyExpectedConditions.visibilityOfFirstElementsInAllFrames(locator);
    }

    @Override
    public Function<WebDriver, WebElement> visibility(By locator) {
        return TeasyExpectedConditions.visibilityOfElementLocatedBy(locator);
    }

    @Override
    public Function<WebDriver, List<WebElement>> presenceOfList(By locator) {
        return TeasyExpectedConditions.presenceOfAllElementsInAllFrames(locator);
    }

    @Override
    public Function<WebDriver, WebElement> presence(By locator) {
        return TeasyExpectedConditions.presenceOfElementLocatedBy(locator);
    }
}
