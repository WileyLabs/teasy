package com.wiley.elements.conditions.elements;

import com.wiley.elements.TeasyElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ElementsNotHaveAttribute implements ExpectedCondition<Boolean> {

    private final List<TeasyElement> els;
    private final String attrName;
    private List<TeasyElement> errorEls;

    public ElementsNotHaveAttribute(List<TeasyElement> els, String attrName) {
        this.els = els;
        this.attrName = attrName;
    }

    @Nullable
    @Override
    public Boolean apply(@Nullable WebDriver webDriver) {
        errorEls = new ArrayList<>();
        boolean isCorrect = true;
        for (TeasyElement el : els) {
            if (el.getAttribute(attrName) != null) {
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
                    .append(el.toString())
                    .append("|");
        }
        return String.format("Elements |%s have attribute '%s'!", error.toString(), attrName);
    }
}
