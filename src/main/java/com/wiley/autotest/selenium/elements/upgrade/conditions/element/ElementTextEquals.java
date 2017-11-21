package com.wiley.autotest.selenium.elements.upgrade.conditions.element;

import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;

public class ElementTextEquals implements ExpectedCondition<Boolean> {

    private final TeasyElement element;
    private final String text;

    public ElementTextEquals(TeasyElement element, String text) {
        this.element = element;
        this.text = text;
    }

    @Nullable
    @Override
    public Boolean apply(@Nullable WebDriver driver) {
        return element.getText().equals(text);
    }

    @Override
    public String toString() {
        return String.format("Element '%s' text is not '%s'", element.toString(), text);
    }
}
