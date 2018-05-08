package com.wiley.autotest.selenium.elements;

import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;

/**
 * Base interface for custom objects
 * e.g. Buttons, Links, CheckBoxes, Selects etc.
 */
public interface Element {

    TeasyElement getWrappedElement();
}
