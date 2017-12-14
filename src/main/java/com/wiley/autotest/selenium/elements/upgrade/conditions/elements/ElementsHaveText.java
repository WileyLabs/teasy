package com.wiley.autotest.selenium.elements.upgrade.conditions.elements;

import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import com.wiley.autotest.selenium.elements.upgrade.conditions.element.ElementHaveAnyText;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ElementsHaveText implements ExpectedCondition<Boolean> {

    private final List<TeasyElement> elements;
    private final String text;
    private List<TeasyElement> errorElements;

    public ElementsHaveText(List<TeasyElement> elements, String text) {
        this.elements = elements;
        this.text = text;
    }

    @Nullable
    @Override
    public Boolean apply(@Nullable WebDriver webDriver) {
        List<String> actualTexts = new ArrayList<>();
        errorElements = new ArrayList<>();
        boolean isCorrect = true;
        if (text != null) {
            for (TeasyElement el : elements) {
                actualTexts.add(el.getText());
                if (!text.equals(el.getText())) {
                    isCorrect = false;
                    errorElements.add(el);
                }
            }
        } else {
            for (TeasyElement el : elements) {
                if (el.getText() == null || el.getText().equals("")){
                    isCorrect = false;
                    errorElements.add(el);
                }
            }
        }
        return isCorrect;
    }

    @Override
    public String toString() {
        StringBuilder error = new StringBuilder();
        for (TeasyElement el : errorElements) {
            error.append(el.toString()).append(" with text '").append(el.getText()).append("|");
        }
        return String.format("Elements |%s text is wrong! Expected text is %s", error.toString(), text);
    }
}
