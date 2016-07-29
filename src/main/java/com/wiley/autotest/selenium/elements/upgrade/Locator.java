package com.wiley.autotest.selenium.elements.upgrade;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * User: vefimov
 * Date: 27.08.2014
 * Time: 15:48
 */
public interface Locator {
    WebElement locate();

    By getByLocator();
}
