package com.wiley.elements.conditions.element;

import com.wiley.elements.TeasyElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;

public class ElementAbsent implements ExpectedCondition<Boolean> {

    private final TeasyElement el;

    public ElementAbsent(TeasyElement el) {
        this.el = el;
    }

    @Nullable
    @Override
    public Boolean apply(@Nullable WebDriver driver) {
        try {
            return !el.isDisplayed();
        } catch (Throwable ignored) {
            //in case of any exception in TeasyElement considering that element is absent
            return true;
        }
    }

    @Override
    public String toString() {
        return String.format("Element '%s' is not absent!", el.toString());
    }
}
