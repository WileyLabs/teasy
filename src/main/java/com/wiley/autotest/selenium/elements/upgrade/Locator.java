package com.wiley.autotest.selenium.elements.upgrade;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Provides the logic to find element
 */
public interface Locator {
    WebElement find();

    By getBy();
}
