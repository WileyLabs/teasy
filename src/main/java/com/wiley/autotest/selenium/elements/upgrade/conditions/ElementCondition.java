package com.wiley.autotest.selenium.elements.upgrade.conditions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.function.Function;

/**
 * Created by vefimov on 25/05/2017.
 */
public interface ElementCondition {

    Function<WebDriver, List<WebElement>> visibilities(By locator);

    Function<WebDriver, WebElement> visibility(By locator);

    Function<WebDriver, List<WebElement>> presences(By locator);

    Function<WebDriver, WebElement> presence(By locator);

}
