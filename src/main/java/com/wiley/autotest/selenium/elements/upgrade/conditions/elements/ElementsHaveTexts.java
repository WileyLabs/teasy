package com.wiley.autotest.selenium.elements.upgrade.conditions.elements;

import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ElementsHaveTexts implements ExpectedCondition<Boolean> {

    private final List<TeasyElement> elements;
    private final List<String> texts;
    private List<TeasyElement> errorElements;

    public ElementsHaveTexts(List<TeasyElement> elements, List<String> texts) {
        this.elements = elements;
        this.texts = texts;
    }

    @Nullable
    @Override
    public Boolean apply(@Nullable WebDriver webDriver) {
        List<String> actualTexts = new ArrayList<>();
        errorElements = new ArrayList<>();
        boolean haveAllTexts = true;
        for (TeasyElement el : elements) {
            actualTexts.add(el.getText());
            if (!texts.contains(el.getText())) {
                haveAllTexts = false;
                errorElements.add(el);
            }
        }
        return haveAllTexts && actualTexts.size() == texts.size();
    }

    @Override
    public String toString() {
        StringBuilder error = new StringBuilder();
        for (TeasyElement el : errorElements) {
            error.append(el.toString()).append(" with text '").append(el.getText()).append("'|");
        }
        return String.format("Elements |%s text is not present in the expected texts! Expected texts are %s", error.toString(), texts);
    }
}
