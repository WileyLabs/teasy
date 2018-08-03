package com.wiley.elements.types;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Abstraction for repeated location of an element.
 * To match different cases of finding an element it
 * has
 *
 * {@link #find()} - method that is responsible for a element loop up logic
 * {@link #getBy()} - method that provided the locator
 */
public interface Locatable {

    WebElement find();

    By getBy();
}
