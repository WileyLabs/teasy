package com.wiley.elements.conditions.elements;

import com.wiley.elements.TeasyElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ElementsHaveNoText implements ExpectedCondition<Boolean> {

    private final List<TeasyElement> els;
    private List<TeasyElement> errorEls;

    public ElementsHaveNoText(List<TeasyElement> els) {
        this.els = els;
    }

    @Nullable
    @Override
    public Boolean apply(@Nullable WebDriver webDriver) {
        errorEls = new ArrayList<>();
        boolean isCorrect = true;
        for (TeasyElement el : els) {
            if (!el.getText().isEmpty()) {
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
                    .append(String.format("Element '%s' has text '%s'", el.toString(), el.getText()))
                    .append("\n");
        }
        return error.toString();
    }
}
