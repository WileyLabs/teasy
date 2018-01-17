package com.wiley.autotest.framework.pages;

import com.wiley.autotest.actions.RepeatableAction;
import com.wiley.autotest.selenium.context.AbstractPage;
import com.wiley.autotest.selenium.context.SearchStrategy;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

/**
 * Created by shekhavtsov on 29/09/2017.
 */
@Component
public class RepeaterPage extends AbstractPage {

    public RepeaterPage negativeCheck() {
        new RepeatableAction(this::clickOnFailure).perform();
        return this;
    }

    public RepeaterPage positiveCheck() {
        new RepeatableAction(this::clickOnSuccess).perform();
        return this;
    }

    private boolean clickOnFailure() {
        clickElement("[id='testDiv1']");
        return isElementDisplayed();
    }

    private boolean clickOnSuccess() {
        clickElement("[id='testDiv2']");
        return isElementDisplayed();
    }

    private void clickElement(String cssSelector) {
        element(By.cssSelector(cssSelector)).click();
    }

    private boolean isElementDisplayed() {
        return element(By.cssSelector("[id='testDiv3']"), new SearchStrategy(1).nullOnFailure()) != null;
    }
}
