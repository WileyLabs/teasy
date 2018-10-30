package com.wiley.elements;

import org.openqa.selenium.By;

/**
 * Throw when any action on {@link com.wiley.elements.types.NullTeasyElement} is performed
 */
public class NotFoundElException extends RuntimeException {

    public NotFoundElException(By locator) {
        super("Unable to find element with locatable '" + locator + "'");
    }
}
