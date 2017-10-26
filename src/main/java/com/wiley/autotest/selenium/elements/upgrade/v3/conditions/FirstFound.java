package com.wiley.autotest.selenium.elements.upgrade.v3.conditions;

import com.wiley.autotest.TeasyExpectedConditions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.function.Function;

/**
 * Created by vefimov on 25/05/2017.
 */
public class FirstFound implements ElementCondition {

    @Override
    public Function<WebDriver, List<WebElement>> visibility(By locator) {
        return TeasyExpectedConditions.visibilityOfFirstElements(locator);
    }

    @Override
    public Function<WebDriver, List<WebElement>> presence(By locator) {
        return ExpectedConditions.presenceOfAllElementsLocatedBy(locator);
    }
}
