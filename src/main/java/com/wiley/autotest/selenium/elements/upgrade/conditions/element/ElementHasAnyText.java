package com.wiley.autotest.selenium.elements.upgrade.conditions.element;

import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;

public class ElementHasAnyText implements ExpectedCondition<Boolean> {

    private final TeasyElement el;

    public ElementHasAnyText(TeasyElement el) {
        this.el = el;
    }

    @Nullable
    @Override
    public Boolean apply(@Nullable WebDriver driver) {
        return el.getText() != null && !el.getText().trim().equals("");
    }

    @Override
    public String toString() {
        return String.format("Element does not have any text! Actual text is '%s'.",
                el.getText());
    }
}
