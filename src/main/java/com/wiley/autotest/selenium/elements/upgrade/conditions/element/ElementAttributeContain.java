package com.wiley.autotest.selenium.elements.upgrade.conditions.element;

import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;

public class ElementAttributeContain implements ExpectedCondition<Boolean>{

    private final TeasyElement element;
    private final String attributeName;
    private final String value;

    public ElementAttributeContain(TeasyElement element, String attributeName, String value) {
        this.element = element;
        this.attributeName = attributeName;
        this.value = value;
    }

    @Nullable
    @Override
    public Boolean apply(@Nullable WebDriver driver) {
        String attribute = element.getAttribute(attributeName);
        return attribute != null && attribute.contains(value);
    }

    @Override
    public String toString() {
        return String.format("Element '%s' attribute '%s' does not contain value '%s'!", element.toString(), attributeName, value);
    }
}
