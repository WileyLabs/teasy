package com.wiley.elements.find;

import com.wiley.elements.SearchStrategy;
import com.wiley.elements.TeasyFluentWait;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Finding alert
 */
public class AlertLookUp implements LookUp<Alert> {

    private final TeasyFluentWait<WebDriver> fluentWait;

    public AlertLookUp(WebDriver driver, SearchStrategy strategy) {
        this.fluentWait = new TeasyFluentWait<>(driver, strategy);
    }

    public Alert find() {
        return fluentWait.waitFor(ExpectedConditions.alertIsPresent());
    }
}
