package com.wiley.elements.conditions.elements;

import com.wiley.elements.TeasyElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;
import java.util.List;

public class ElementsHaveSize implements ExpectedCondition<Boolean> {

    private final List<TeasyElement> els;
    private final int expectedSize;

    public ElementsHaveSize(List<TeasyElement> els, int expectedSize) {
        this.els = els;
        this.expectedSize = expectedSize;
    }

    @Nullable
    @Override
    public Boolean apply(@Nullable WebDriver webDriver) {
        return els.size() == expectedSize;
    }

    @Override
    public String toString() {
        return String.format("Elements does not have expected size. Expected '%s'. Actual '%s'", expectedSize, els.size());
    }
}
