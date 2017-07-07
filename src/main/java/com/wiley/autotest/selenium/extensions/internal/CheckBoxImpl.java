package com.wiley.autotest.selenium.extensions.internal;

import com.wiley.autotest.selenium.elements.CheckBox;
import com.wiley.autotest.selenium.elements.upgrade.OurWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CheckBoxImpl extends AbstractEnabledElement implements CheckBox {
    protected CheckBoxImpl(final OurWebElement wrappedElement) {
        super(wrappedElement);
    }

    protected CheckBoxImpl(final OurWebElement wrappedElement, By by) {
        super(wrappedElement, by);
    }

    @Override
    public void clickWithJavaScript() {
        executeScript("arguments[0].click();", getWrappedWebElement());
    }

    @Override
    public void click() {
        getWrappedWebElement().click();
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
            if (inputTags.get(0).getAttribute("class").contains("customCheckbox")) {
                String inputId = inputTags.get(0).getAttribute("id");
                return (Boolean) executeScript("return document.getElementById('" + inputId + "').checked;");
            }
        }
        return getWrappedElement().isSelected();
    }

    @Override
    public OurWebElement getWrappedWebElement() {
        return getWrappedElement();
    }

    @Override
    public boolean isEnabled() {
        List<WebElement> inputTags = getWrappedWebElement().findElements(By.xpath("./../input"));
        if (!inputTags.isEmpty()) {
            if (inputTags.get(0).getAttribute("class").contains("customCheckbox")) {
                String inputId = inputTags.get(0).getAttribute("id");
                return !(Boolean) executeScript("return document.getElementById('" + inputId + "').disabled;");
            }
        }
        return getWrappedElement().isEnabled();
    }
}