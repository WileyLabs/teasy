package com.wiley.autotest.framework.pages;

import com.wiley.autotest.selenium.context.AbstractPage;
import com.wiley.autotest.selenium.context.SearchStrategy;
import com.wiley.autotest.selenium.elements.upgrade.DomTeasyElement;
import com.wiley.autotest.selenium.elements.upgrade.NullTeasyElement;
import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import com.wiley.autotest.selenium.elements.upgrade.VisibleTeasyElement;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

/**
 * Created by shekhavtsov on 20/07/2017.
 */
@Component
public class TestDomElementPage extends AbstractPage {

    public TestDomElementPage checkFirstVisibleElement() {
        element(By.cssSelector("li")).should().beDisplayed();
        element(By.cssSelector("li")).should().haveText("element #1 (visible)");
        return this;
    }

    public TestDomElementPage checkElementBecomeVisible() {
        element(By.id("waitElement")).should().beDisplayed();
        return this;
    }

    //    The element changes coordinates. Each check validates element with different x, y.
    public TestDomElementPage checkAnimationElement() {
        element(By.id("myAnimation")).should().beDisplayed();
        element(By.id("myAnimation")).should().beDisplayed();
        element(By.id("myAnimation")).should().beDisplayed();
        return this;
    }

    public TestDomElementPage checkElementInFrame() {
        element(By.id("elementInFrame")).should().beDisplayed();
        element(By.id("elementInFrame")).should().haveText("This page is displayed in an iframe #3");
        return this;
    }

    public TestDomElementPage checkElementNotFound() {
        element(By.id("elementNonexistent")).should().beDisplayed();
        return this;
    }

    public TestDomElementPage checkElementInstanceOfVisibleElement() {
        TeasyElement element = element(By.cssSelector("li"));
        assertTrue(element instanceof VisibleTeasyElement);
        return this;
    }

    public TestDomElementPage checkDomElementInstanceOfDomElement() {
        TeasyElement element = domElement(By.cssSelector("li"));
        assertTrue(element instanceof DomTeasyElement);
        return this;
    }

    public TestDomElementPage checkNonExistingElementInstanceOfNullElement() {
        TeasyElement element = element(By.cssSelector("not_present_element"), new SearchStrategy(1));
        assertTrue(element instanceof NullTeasyElement);
        return this;
    }

    public TestDomElementPage checkNonExistingElementWithNullOnFailure() {
        TeasyElement element = element(By.cssSelector("not_present_element"), new SearchStrategy(1).nullOnFailure());
        assertNull(element);
        return this;
    }

    public TestDomElementPage checkNonExistingElementShouldBeAbsent() {
        TeasyElement element = element(By.cssSelector("not_present_element"), new SearchStrategy(1));
        element.should().beAbsent();
        return this;
    }

    public TestDomElementPage checkNonExistingElementClickFails() {
        element(By.cssSelector("not_present_element"), new SearchStrategy(1)).click();
        return this;
    }
}
