package com.wiley.autotest.selenium.extensions.internal;

import com.wiley.autotest.selenium.elements.RadioButton;
import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class RadioButtonImpl extends AbstractEnabledElement implements RadioButton {
    protected RadioButtonImpl(final TeasyElement wrappedElement) {
        super(wrappedElement);
    }

    protected RadioButtonImpl(final TeasyElement wrappedElement, By by) {
        super(wrappedElement, by);
    }

    @Override
    public void clickWithJavaScript() {
        executeScript("arguments[0].click();", getWrappedWebElement());
    }


    @Override
    public void click() {
        getWrappedElement().click();
    }

    @Override
    public void clickWithReload() {
        getWrappedElement().click();
    }

    @Override
    public boolean isClickable() {
        return isEnabled() && isSelected();
    }

    @Override
    public boolean isSelected() {
        List<WebElement> inputTags = getWrappedWebElement().findElements(By.xpath("./../input"));
        if (!inputTags.isEmpty()) {
            if (inputTags.get(0).getAttribute("class").contains("customRadio")) {
                String inputId = inputTags.get(0).getAttribute("id");
                return (Boolean) executeScript("return document.getElementById('" + inputId + "').checked;");
            }
        }
        return getWrappedElement().isSelected();
    }

    @Override
    public boolean isEnabled() {
        List<WebElement> inputTags = getWrappedWebElement().findElements(By.xpath("./../input"));
        if (!inputTags.isEmpty()) {
            if (inputTags.get(0).getAttribute("class").contains("customRadio")) {
                String inputId = inputTags.get(0).getAttribute("id");
                return !(Boolean) executeScript("return document.getElementById('" + inputId + "').disabled;");
            }
        }
        return getWrappedElement().isEnabled();
    }

    @Override
    public TeasyElement getWrappedWebElement() {
        return getWrappedElement();
    }
}
