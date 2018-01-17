package com.wiley.autotest.selenium.elements.upgrade.conditions.elements;

import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ElementsNotHaveAttribute implements ExpectedCondition<Boolean> {

    private final List<TeasyElement> elements;
    private final String attributeName;
    private List<TeasyElement> errorElements;

    public ElementsNotHaveAttribute(List<TeasyElement> elements, String attributeName) {
        this.elements = elements;
        this.attributeName = attributeName;
    }

    @Nullable
    @Override
    public Boolean apply(@Nullable WebDriver webDriver) {
        errorElements = new ArrayList<>();
        boolean isCorrect = true;
        for (TeasyElement el : elements) {
            if (el.getAttribute(attributeName) != null) {
                isCorrect = false;
                errorElements.add(el);
            }
        }
        return isCorrect;
    }

    @Override
    public String toString() {
        StringBuilder error = new StringBuilder();
        for (TeasyElement el : errorElements) {
            error.append(el.toString()).append("|");
        }
        return String.format("Elements |%s have attribute '%s'!", error.toString(), attributeName);
    }
}
