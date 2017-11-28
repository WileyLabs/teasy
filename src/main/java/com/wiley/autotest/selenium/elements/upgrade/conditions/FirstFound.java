package com.wiley.autotest.selenium.elements.upgrade.conditions;

import com.wiley.autotest.TeasyExpectedConditions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.function.Function;

/**
 * Created by vefimov on 25/05/2017.
 */
public class FirstFound implements ElementCondition {

    @Override
    public Function<WebDriver, List<WebElement>> visibilities(By locator) {
        return TeasyExpectedConditions.visibilityOfFirstElements(locator);
    }

    @Override
    public Function<WebDriver, WebElement> visibility(By locator) {
        return TeasyExpectedConditions.visibilityOfElementLocatedBy(locator);
    }

    @Override
    public Function<WebDriver, List<WebElement>> presences(By locator) {
        return TeasyExpectedConditions.presenceOfAllElementsLocatedBy(locator);
    }

    @Override
    public Function<WebDriver, WebElement> presence(By locator) {
        return TeasyExpectedConditions.presenceOfElementLocatedBy(locator);
    }
}
