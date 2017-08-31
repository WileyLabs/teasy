package com.wiley.autotest.selenium.elements;

import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;

public interface WebContainer extends Element {
    void init(TeasyElement wrappedElement);
}
