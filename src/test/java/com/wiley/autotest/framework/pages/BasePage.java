package com.wiley.autotest.framework.pages;

import com.wiley.autotest.selenium.context.AbstractPage;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

/**
 * User: ntyukavkin
 * Date: 19.05.2017
 * Time: 14:20
 */
@Component
public class BasePage extends AbstractPage {

    public BasePage checkIframeIsPresent() {
        assertElementIsDisplayed(By.cssSelector("iframe"));
        return this;
    }

    public BasePage checkIframeCountIsTwo() {
        assertEquals(domElements(By.cssSelector("iframe")).size(), 2);
        return this;
    }
}
