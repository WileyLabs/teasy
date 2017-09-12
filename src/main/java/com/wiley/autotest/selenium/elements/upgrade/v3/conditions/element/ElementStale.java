package com.wiley.autotest.selenium.elements.upgrade.v3.conditions.element;

import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;

/**
 * Created by vefimov on 23/08/2017.
 */
public class ElementStale implements ExpectedCondition<Boolean> {

    private TeasyElement element;

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
