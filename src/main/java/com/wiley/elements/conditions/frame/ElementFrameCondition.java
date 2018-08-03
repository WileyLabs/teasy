package com.wiley.elements.conditions.frame;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.function.Function;

/**
 * Abstraction representing different kinds of conditions
 * that might be need while finding elements in frames
 * <p>
 * Usage context is to be implemented by conditions defined by different frame strategies:
 * - Wait for first visible element in first frame
 * - wait for all visible elements in first frame
 * - wait for first present element in all frames
 * - etc.
 */
public interface ElementFrameCondition {

    Function<WebDriver, List<WebElement>> visibilityOfList(By locator);

    Function<WebDriver, WebElement> visibility(By locator);

    Function<WebDriver, List<WebElement>> presenceOfList(By locator);

    Function<WebDriver, WebElement> presence(By locator);
}
