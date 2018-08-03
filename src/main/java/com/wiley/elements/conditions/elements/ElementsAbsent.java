package com.wiley.elements.conditions.elements;

import com.wiley.elements.TeasyElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ElementsAbsent implements ExpectedCondition<Boolean> {

    private final List<TeasyElement> els;
    private List<TeasyElement> errorEls;

    public ElementsAbsent(List<TeasyElement> els) {
        this.els = els;
    }

    @Nullable
    @Override
    public Boolean apply(@Nullable WebDriver webDriver) {
        boolean allAbsent = true;
        errorEls = new ArrayList<>();
        for (TeasyElement el : els) {
            if (el.isDisplayed()) {
                allAbsent = false;
                errorEls.add(el);
            }
        }
        return allAbsent;
    }

    @Override
    public String toString() {
        StringBuilder error = new StringBuilder();
        for (TeasyElement el : errorEls) {
            error
                    .append(el.toString())
                    .append("| ");
        }
        return String.format("Elements |%s are not absent!", error.toString());
    }
}
