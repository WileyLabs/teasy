package com.wiley.elements.conditions;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;

/**
 * Created by vefimov on 25/08/2017.
 */
public class PageLoaded implements ExpectedCondition<Boolean> {


    @Nullable
    @Override
    public Boolean apply(@Nullable WebDriver driver) {
        return "complete".equals(((JavascriptExecutor) driver).executeScript("return document.readyState"));
    }

    @Override
    public String toString() {
        return "Page is not loaded";
    }
}
