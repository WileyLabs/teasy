package com.wiley.autotest.selenium.extensions.internal;

import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import org.openqa.selenium.By;

import java.util.List;

import static com.wiley.autotest.utils.JsActions.executeScript;

public class CheckBox extends AbstractElement {
    public CheckBox(final TeasyElement element) {
        super(element);
    }

    public void clickWithJavaScript() {
        executeScript("arguments[0].click();", getWrappedElement());
    }

    public void click() {
        getWrappedElement().click();
    }

    public void clickWithReload() {
        getWrappedElement().click();
    }

    public boolean isClickable() {
        return isEnabled() && isSelected();
    }

    public boolean isSelected() {
        List<TeasyElement> inputTags = getWrappedElement().domElements(By.xpath("./../input"));
        if (!inputTags.isEmpty()) {
            if (inputTags.get(0).getAttribute("class").contains("customCheckbox")) {
                String inputId = inputTags.get(0).getAttribute("id");
                return (Boolean) executeScript("return document.getElementById('" + inputId + "').checked;");
            }
        }
        return getWrappedElement().isSelected();
    }

    public boolean isEnabled() {
        List<TeasyElement> inputTags = getWrappedElement().domElements(By.xpath("./../input"));
        if (!inputTags.isEmpty()) {
            if (inputTags.get(0).getAttribute("class").contains("customCheckbox")) {
                String inputId = inputTags.get(0).getAttribute("id");
                return !(Boolean) executeScript("return document.getElementById('" + inputId + "').disabled;");
            }
        }
        return getWrappedElement().isEnabled();
    }
}