package com.wiley.elements.find;

import org.openqa.selenium.By;
/**
 * Basic interface for finding objects by locator {@link By} of a generic type
 * @param <T> - object type to find
 */
public interface LookUpWithLocator<T> {

    T find(By locator);
}
