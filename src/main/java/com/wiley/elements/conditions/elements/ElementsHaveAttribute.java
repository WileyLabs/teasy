package com.wiley.elements.conditions.elements;

import com.wiley.elements.TeasyElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ElementsHaveAttribute implements ExpectedCondition<Boolean> {

    private final List<TeasyElement> els;
    private final String attrName;
    private List<TeasyElement> errorEls;

    public ElementsHaveAttribute(List<TeasyElement> els, String attributeName) {
        this.els = els;
        this.attrName = attributeName;
    }

    @Nullable
    @Override
    public Boolean apply(@Nullable WebDriver webDriver) {
        errorEls = new ArrayList<>();
        boolean isCorrect = true;
        for (TeasyElement el : els) {
            if (el.getAttribute(attrName) == null) {
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
        return String.format("Elements |%s does not have attribute '%s'!", error.toString(), attrName);
    }
}
