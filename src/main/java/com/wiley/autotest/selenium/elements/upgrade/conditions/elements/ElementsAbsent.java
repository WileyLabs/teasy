package com.wiley.autotest.selenium.elements.upgrade.conditions.elements;

import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ElementsAbsent implements ExpectedCondition<Boolean> {

    private final List<TeasyElement> elements;
    private List<TeasyElement> errorElements;

    public ElementsAbsent(List<TeasyElement> elements) {
        this.elements = elements;
    }

    @Nullable
    @Override
    public Boolean apply(@Nullable WebDriver webDriver) {
        boolean allAbsent = true;
        errorElements = new ArrayList<>();
        for (TeasyElement el : elements) {
            if (el.isDisplayed()) {
                allAbsent = false;
                errorElements.add(el);
            }
        }
        return allAbsent;
    }

    @Override
    public String toString() {
        StringBuilder error = new StringBuilder();
        for (TeasyElement el : errorElements) {
            error.append(el.toString()).append("| ");
        }
        return String.format("Elements |%s is not absent!", error.toString());
    }
}
