package com.wiley.elements.conditions.elements;

import com.wiley.elements.TeasyElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ElementsHaveTexts implements ExpectedCondition<Boolean> {

    private final List<TeasyElement> els;
    private final List<String> texts;
    private List<TeasyElement> errorEls;

    public ElementsHaveTexts(List<TeasyElement> els, List<String> texts) {
        this.els = els;
        this.texts = texts;
    }

    @Nullable
    @Override
    public Boolean apply(@Nullable WebDriver webDriver) {
        List<String> actualTexts = new ArrayList<>();
        errorEls = new ArrayList<>();
        boolean haveAllTexts = true;
        for (TeasyElement el : els) {
            actualTexts.add(el.getText());
            if (!texts.contains(el.getText())) {
                haveAllTexts = false;
                errorEls.add(el);
            }
        }
        return haveAllTexts && actualTexts.size() == texts.size();
    }

    @Override
    public String toString() {
        StringBuilder error = new StringBuilder();
        for (TeasyElement el : errorEls) {
            error
                    .append(el.toString())
                    .append(" with text '")
                    .append(el.getText())
                    .append("'|");
        }
        return String.format("Elements |%s text are not present in the expected texts! Expected texts are %s", error.toString(), texts);
    }
}
