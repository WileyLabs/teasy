package com.wiley.autotest.selenium.driver.events.listeners;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

public class DisableJQueryAnimationEventListener extends AbstractWebDriverEventListener {
    @Override
    public void beforeClickOn(final WebElement element, final WebDriver driver) {
        disableJQueryAnimation(driver);
    }

    @Override
    public void afterNavigateTo(final String url, final WebDriver driver) {
        disableJQueryAnimation(driver);
    }

    @Override
    public void afterNavigateBack(final WebDriver driver) {
        disableJQueryAnimation(driver);
    }

    @Override
    public void afterNavigateForward(final WebDriver driver) {
        disableJQueryAnimation(driver);
    }

    private void disableJQueryAnimation(final WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript(disableAnimationScript);
    }

    private final String disableAnimationScript = new StringBuilder("if(window.jQuery!=undefined)")
            .append("if(jQuery.fn.jquery == '1.2.6') {")
            .append("jQuery.fx.speeds = {def: 0, slow: 0, fast: 0};")
            .append("} else {")
            .append("jQuery.fx.off = true;")
            .append("}")
            .toString();
}
