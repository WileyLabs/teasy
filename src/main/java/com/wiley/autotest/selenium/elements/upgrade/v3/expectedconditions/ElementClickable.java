package com.wiley.autotest.selenium.elements.upgrade.v3.expectedconditions;

import com.wiley.autotest.selenium.elements.upgrade.OurWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;

/**
 * Created by vefimov on 24/08/2017.
 */
public class ElementClickable implements ExpectedCondition<Boolean> {

    private OurWebElement element;

    public ElementClickable(OurWebElement element) {
        this.element = element;
    }

    @Nullable
    @Override
    public Boolean apply(@Nullable WebDriver driver) {
        return element.isDisplayed() && element.isEnabled();
    }

    @Override
    public String toString() {
        return String.format("Element '%s' is not clickable", element.toString());
    }

}
