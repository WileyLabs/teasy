package com.wiley.elements.conditions.elements;

import com.wiley.elements.TeasyElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ElementsHaveAnyText implements ExpectedCondition<Boolean> {

    private final List<TeasyElement> els;
    private List<TeasyElement> errorEls;

    public ElementsHaveAnyText(List<TeasyElement> els) {
        this.els = els;
    }

    @Nullable
    @Override
    public Boolean apply(@Nullable WebDriver webDriver) {
        errorEls = new ArrayList<>();
        boolean isCorrect = true;
        for (TeasyElement el : els) {
            if (el.getText() == null || el.getText().trim().equals("")) {
                isCorrect = false;
                errorEls.add(el);
            }
        }
        return isCorrect;
    }

    @Override
    public String toString() {
        StringBuilder error = new StringBuilder();
        for (TeasyElement el : errorEls) {
            error
                    .append(String.format("Element |%s| does not have any text! Actual text is '%s'.",
                            el, el.getText()));
        }
        return error.toString();
    }
}
