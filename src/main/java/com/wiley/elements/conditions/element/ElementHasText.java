package com.wiley.elements.conditions.element;

import com.wiley.elements.TeasyElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;

public class ElementHasText implements ExpectedCondition<Boolean> {

    private final TeasyElement el;
    private final String text;

    public ElementHasText(TeasyElement el, String text) {
        this.el = el;
        this.text = text;
    }

    @Nullable
    @Override
    public Boolean apply(@Nullable WebDriver driver) {
        return el.getText().equals(text);
    }

    @Override
    public String toString() {
        return String.format("Element '%s' text is not '%s'! Actual text is '%s'.",
                el.toString(), text, el.getText());
    }
}
