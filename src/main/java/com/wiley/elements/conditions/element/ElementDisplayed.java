package com.wiley.elements.conditions.element;

import com.wiley.elements.TeasyElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;

public class ElementDisplayed implements ExpectedCondition<Boolean> {

    private final TeasyElement el;

    public ElementDisplayed(TeasyElement el) {
        this.el = el;
    }

    @Nullable
    @Override
    public Boolean apply(@Nullable WebDriver driver) {
        return el.isDisplayed();
    }

    @Override
    public String toString() {
        return String.format("Element '%s' is not displayed!", el.toString());
    }
}
