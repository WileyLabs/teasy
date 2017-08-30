package com.wiley.autotest.selenium.elements.upgrade.v3.expectedconditions;

import com.wiley.autotest.selenium.elements.upgrade.TeasyWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;

/**
 * Created by vefimov on 21/08/2017.
 */
public class ElementDisplayed implements ExpectedCondition<Boolean> {

    private TeasyWebElement element;

    public ElementDisplayed(TeasyWebElement element) {
        this.element = element;
    }

    @Nullable
    @Override
    public Boolean apply(@Nullable WebDriver driver) {
        return element.isDisplayed();
    }

    @Override
    public String toString() {
        return String.format("Element '%s' is not displayed!", element.toString());
    }
}
