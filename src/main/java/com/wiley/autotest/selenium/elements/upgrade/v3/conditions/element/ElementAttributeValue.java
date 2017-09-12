package com.wiley.autotest.selenium.elements.upgrade.v3.conditions.element;

import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;

/**
 * Created by vefimov on 23/08/2017.
 */
public class ElementAttributeValue implements ExpectedCondition<Boolean> {
    private TeasyElement element;
    private String attributeName;
    private String value;

    public ElementAttributeValue(TeasyElement element, String attributeName, String value) {
        this.element = element;
        this.attributeName = attributeName;
        this.value = value;
    }

    @Nullable
    @Override
    public Boolean apply(@Nullable WebDriver driver) {
        return element.getAttribute(attributeName).equals(value);
    }

    @Override
    public String toString() {
        return String.format("Element '%s' attribute '%s' value is not '%s'", element.toString(), attributeName, value);
    }
}
