package com.wiley.elements.conditions.elements;

import com.wiley.elements.TeasyElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ElementsHaveText implements ExpectedCondition<Boolean> {

    private final List<TeasyElement> els;
    private final String text;
    private List<TeasyElement> errorEls;

    public ElementsHaveText(List<TeasyElement> els, String text) {
        this.els = els;
        this.text = text;
    }

    @Nullable
    @Override
    public Boolean apply(@Nullable WebDriver webDriver) {
        errorEls = new ArrayList<>();
        boolean isCorrect = true;
        for (TeasyElement el : els) {
            if (!text.equals(el.getText())) {
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
                    .append(String.format("%s text is incorrect! Expected text is '%s'.", el.toString(), text))
                    .append(String.format("Actual text is '%s'", el.getText()))
                    .append("\n");
        }
        return error.toString();
    }
}
