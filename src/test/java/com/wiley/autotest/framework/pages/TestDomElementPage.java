package com.wiley.autotest.framework.pages;

import com.wiley.autotest.selenium.context.AbstractPage;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

/**
 * Created by shekhavtsov on 29/09/2017.
 */
@Component
public class TestDomElementPage extends AbstractPage {

    public TestDomElementPage checkFirstDomElement() {
        domElement(By.cssSelector("li")).should().haveAttribute("id", "first_li_element");
        return this;
    }

    public TestDomElementPage checkDomElementBecomeVisible() {
        domElement(By.id("waitElement")).should().beDisplayed();
        return this;
    }

    //    The element changes coordinates. Each check validates element with different x, y.
    public TestDomElementPage checkAnimationDomElement() {
        domElement(By.id("myAnimation")).should().beDisplayed();
        domElement(By.id("myAnimation")).should().beDisplayed();
        domElement(By.id("myAnimation")).should().beDisplayed();
        return this;
    }

    public TestDomElementPage checkDomElementInFrame() {
        domElement(By.id("elementInFrame")).should().beDisplayed();
        domElement(By.id("elementInFrame")).should().haveText("This page is displayed in an iframe #3");
        return this;
    }

    public TestDomElementPage checkDomElementNotFound() {
        domElement(By.id("elementNonexistent")).should().beDisplayed();
        return this;
    }

    public TestDomElementPage checkDomElementsReturnAll() {
        assertTrue(domElements(By.tagName("li")).size() == 5);
        return this;
    }
}
