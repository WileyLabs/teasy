package com.wiley.autotest.selenium.extensions.internal;

import com.wiley.autotest.selenium.elements.RadioButton;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.wiley.autotest.utils.ExecutionUtils.isIE;

public class RadioButtonImpl extends AbstractEnabledElement implements RadioButton {
    protected RadioButtonImpl(final WebElement wrappedElement) {
        super(wrappedElement);
    }

    protected RadioButtonImpl(final WebElement wrappedElement, By by) {
        super(wrappedElement, by);
    }

    @Override
    public void clickWithJavaScript() {
        executeScript("arguments[0].click();", getWrappedWebElement());
    }


    @Override
    public void click() {
        if (isIE()) {
            JavascriptExecutor executor = (JavascriptExecutor) getDriver();
            executor.executeScript("arguments[0].click();", getWrappedWebElement());
        } else {
            getWrappedElement().click();
        }
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
    public WebElement getWrappedWebElement() {
        return getWrappedElement();
    }
}
