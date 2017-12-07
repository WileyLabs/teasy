package com.wiley.autotest.selenium.elements.upgrade.conditions.elements;

import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ElementsAttributeValue implements ExpectedCondition<Boolean> {

    private final List<TeasyElement> elements;
    private final String attributeName;
    private final String value;
    private List<TeasyElement> errorElements;

    public ElementsAttributeValue(List<TeasyElement> elements, String attributeName, String value) {
        this.elements = elements;
        this.attributeName = attributeName;
        this.value = value;
    }

    @Nullable
    @Override
    public Boolean apply(@Nullable WebDriver webDriver) {
        errorElements = new ArrayList<>();
        boolean isCorrect = true;
        for (TeasyElement el : elements) {
            if (value.equals(el.getAttribute(attributeName))) {
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
        return String.format("Elements |%s does not have attribute '%s'!", error.toString(), attributeName);
    }
}
