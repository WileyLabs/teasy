package com.wiley.autotest.selenium.elements.upgrade.conditions.element;

import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;

public class ElementStale implements ExpectedCondition<Boolean> {

    private final TeasyElement element;

    public ElementStale(TeasyElement element) {
        this.element = element;
    }

    @Nullable
    @Override
    public Boolean apply(@Nullable WebDriver driver) {
        try {
            element.isEnabled();
            return false;
        } catch (StaleElementReferenceException expectedWhenStale) {
            return true;
        }
    }

    public String toString() {
        return String.format("Element '%s' didn't become stale!", element.toString());
    }

}
