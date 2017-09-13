package com.wiley.autotest.selenium.elements.upgrade.v3.conditions.element;

import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;

public class ElementAbsent implements ExpectedCondition<Boolean> {

    private TeasyElement element;

    public ElementAbsent(TeasyElement element) {
        this.element = element;
    }

    @Nullable
    @Override
    public Boolean apply(@Nullable WebDriver driver) {
        try {
            return !element.isDisplayed();
        } catch (Throwable ignored) {
            //in case of any exception in TeasyElement considering that element is absent
            return true;
        }
    }

    @Override
    public String toString() {
        return String.format("Element '%s' is not absent!", element.toString());
    }
}
