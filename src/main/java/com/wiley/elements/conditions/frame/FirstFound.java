package com.wiley.elements.conditions.frame;

import com.wiley.elements.conditions.TeasyExpectedConditions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.function.Function;

/**
 * Conditions will perform search for element(s) in All frames
 * but will end as soon as first element(s) is found
 * i.e. if you have 3 frames and elements by given locator are found in 1st frame
 * the search will not go into next frames. If you need to find in all frames
 * use  {@link FirstFoundInAllFramesInContext}
 */
public class FirstFound implements ElementFrameCondition {

    @Override
    public Function<WebDriver, List<WebElement>> visibilityOfList(By locator) {
        return TeasyExpectedConditions.visibilityOfFirstElements(locator);
    }

    @Override
    public Function<WebDriver, WebElement> visibility(By locator) {
        return TeasyExpectedConditions.visibilityOfElementLocatedBy(locator);
    }

    @Override
    public Function<WebDriver, List<WebElement>> presenceOfList(By locator) {
        return TeasyExpectedConditions.presenceOfAllElementsLocatedBy(locator);
    }

    @Override
    public Function<WebDriver, WebElement> presence(By locator) {
        return TeasyExpectedConditions.presenceOfElementLocatedBy(locator);
    }
}
