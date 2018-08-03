package com.wiley.elements.conditions.elements;

import com.wiley.elements.TeasyElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ElementsAttributeValue implements ExpectedCondition<Boolean> {

    private final List<TeasyElement> els;
    private final String attributeName;
    private final String value;
    private List<TeasyElement> errorEls;

    public ElementsAttributeValue(List<TeasyElement> els, String attrName, String value) {
        this.els = els;
        this.attributeName = attrName;
        this.value = value;
    }

    @Nullable
    @Override
    public Boolean apply(@Nullable WebDriver webDriver) {
        errorEls = new ArrayList<>();
        boolean isCorrect = true;
        for (TeasyElement el : els) {
            if (value.equals(el.getAttribute(attributeName))) {
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
            error.append(String.format("Element |%s| does not have attribute '%s'!",
                            el.toString(), attributeName));
        }
        return error.toString();
    }
}
