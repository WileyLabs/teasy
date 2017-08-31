package com.wiley.autotest.selenium.elements.upgrade.v3.expectedconditions;

import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;

/**
 * Created by vefimov on 22/08/2017.
 */
public class ElementTextEquals implements ExpectedCondition<Boolean> {
    private TeasyElement element;
    private String text;

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
