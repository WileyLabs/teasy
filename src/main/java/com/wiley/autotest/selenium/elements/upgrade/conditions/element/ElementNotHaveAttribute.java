package com.wiley.autotest.selenium.elements.upgrade.conditions.element;

import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;

public class ElementNotHaveAttribute implements ExpectedCondition<Boolean> {

    private final TeasyElement el;
    private String attrName;

    public ElementNotHaveAttribute(TeasyElement el, String attrName) {
        this.el = el;
        this.attrName = attrName;
    }

    @Nullable
    @Override
    public Boolean apply(@Nullable WebDriver driver) {
        return el.getAttribute(attrName) == null;
    }

    @Override
    public String toString() {
        return String.format("Element '%s' has attribute '%s'!",
                el.toString(), attrName);
    }

}
