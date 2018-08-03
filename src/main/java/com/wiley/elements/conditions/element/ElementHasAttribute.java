package com.wiley.elements.conditions.element;

import com.wiley.elements.TeasyElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;

public class ElementHasAttribute implements ExpectedCondition<Boolean> {

    private final TeasyElement el;
    private final String attrName;

    public ElementHasAttribute(TeasyElement el, String attrName) {
        this.el = el;
        this.attrName = attrName;
    }

    @Nullable
    @Override
    public Boolean apply(@Nullable WebDriver driver) {
        return el.getAttribute(attrName) != null;
    }

    @Override
    public String toString() {
        return String.format("Element '%s' does not have attribute '%s'!",
                el.toString(), attrName);
    }
}
