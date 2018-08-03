package com.wiley.elements.conditions.element;

import com.wiley.elements.TeasyElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;

public class ElementAttributeValue implements ExpectedCondition<Boolean> {

    private final TeasyElement el;
    private final String attrName;
    private final String value;

    public ElementAttributeValue(TeasyElement el, String attrName, String value) {
        this.el = el;
        this.attrName = attrName;
        this.value = value;
    }

    @Nullable
    @Override
    public Boolean apply(@Nullable WebDriver driver) {
        return value.equals(el.getAttribute(attrName));
    }

    @Override
    public String toString() {
        return String.format("Element '%s' attribute '%s' value is not '%s'! Actual value is '%s'.",
                el.toString(), attrName, value, el.getAttribute(attrName));
    }
}
