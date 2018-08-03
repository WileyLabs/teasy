package com.wiley.elements.conditions.elements;

import com.wiley.elements.TeasyElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ElementsContainsText implements ExpectedCondition<Boolean> {

    private final List<TeasyElement> els;
    private final String text;
    private List<TeasyElement> errorEls;

    public ElementsContainsText(List<TeasyElement> els, String text) {
        this.els = els;
        this.text = text;
    }

    @Nullable
    @Override
    public Boolean apply(@Nullable WebDriver webDriver) {
        errorEls = new ArrayList<>();
        boolean isCorrect = true;
        for (TeasyElement el : els) {
            if (!el.getText().contains(text)) {
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
                    .append(String.format("Element '%s' not contains text '%s'", el.toString(), text))
                    .append(String.format("Actual text is '%s'", el.getText()))
                    .append("\n");
        }
        return error.toString();
    }
}
