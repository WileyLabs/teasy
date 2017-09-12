package com.wiley.autotest.selenium.extensions.internal;

import com.wiley.autotest.selenium.elements.EnabledElement;
import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import com.wiley.autotest.selenium.elements.upgrade.VisibleTeasyElement;
import org.openqa.selenium.By;

abstract class AbstractEnabledElement extends AbstractElement implements EnabledElement {

    protected AbstractEnabledElement(final TeasyElement wrappedElement) {
        super(wrappedElement);
    }

    protected AbstractEnabledElement(final TeasyElement wrappedElement, final By by) {
        super(wrappedElement, by);
    }

    @Override
    public boolean isEnabled() {
        return getWrappedElement().isDisplayed() && getWrappedElement().isEnabled();
    }

    @Override
    public TeasyElement getWrappedWebElement() {
        return getWrappedElement();
    }
}
