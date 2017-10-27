package com.wiley.autotest.selenium.elements.upgrade.v3.conditions;

import com.wiley.autotest.ExpectedConditions2;
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
        return ExpectedConditions2.visibilityOfFirstElements(locator);
    }

    @Override
    public Function<WebDriver, WebElement> visibility(By locator) {
        return ExpectedConditions2.visibilityOfElementLocatedBy(locator);
    }

    @Override
    public Function<WebDriver, List<WebElement>> presences(By locator) {
        return ExpectedConditions2.presenceOfAllElementsLocatedBy(locator);
    }

    @Override
    public Function<WebDriver, WebElement> presence(By locator) {
        return ExpectedConditions2.presenceOfElementLocatedBy(locator);
    }
}
