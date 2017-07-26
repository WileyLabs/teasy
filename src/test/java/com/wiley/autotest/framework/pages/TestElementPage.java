package com.wiley.autotest.framework.pages;

import com.wiley.autotest.selenium.context.AbstractPage;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

/**
 * Created by shekhavtsov on 20/07/2017.
 */
@Component
public class TestElementPage extends AbstractPage {

    public TestElementPage checkFirstVisibleElement() {
        element(By.cssSelector("li")).should().beDisplayed();
        element(By.cssSelector("li")).should().haveText("element #1 (visible)");
        return this;
    }

    public TestElementPage checkElementBecomeVisible() {
        element(By.id("waitElement")).should().beDisplayed();
        return this;
    }

    //    The element changes coordinates. Each check validates element with different x, y.
    public TestElementPage checkAnimationElement() {
        element(By.id("myAnimation")).should().beDisplayed();
        element(By.id("myAnimation")).should().beDisplayed();
        element(By.id("myAnimation")).should().beDisplayed();
        return this;
    }

    public TestElementPage checkElementInFrame() {
        element(By.id("elementInFrame")).should().beDisplayed();
        element(By.id("elementInFrame")).should().haveText("This page is displayed in an iframe #3");
        return this;
    }

    public TestElementPage checkElementNotFound() {
        element(By.id("elementNonexistent")).should().beDisplayed();
        return this;
    }


}
