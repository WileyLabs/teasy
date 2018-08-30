package com.wiley.autotest.framework.pages;

import com.wiley.elements.types.DomTeasyElement;
import com.wiley.elements.types.NullTeasyElement;
import com.wiley.elements.types.VisibleTeasyElement;
import com.wiley.page.BasePage;
import com.wiley.elements.*;
import com.wiley.utils.JsActions;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;

/**
 * Created by shekhavtsov on 20/07/2017.
 */
public class TestElementPage extends BasePage {

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

    public TestElementPage checkElementInstanceOfVisibleElement() {
        TeasyElement el = element(By.cssSelector("li"));
        Assertions.assertThat(el).isInstanceOf(VisibleTeasyElement.class);
        return this;
    }

    public TestElementPage checkDomElementInstanceOfDomElement() {
        TeasyElement el = domElement(By.cssSelector("li"));
        Assertions.assertThat(el).isInstanceOf(DomTeasyElement.class);
        return this;
    }

    public TestElementPage checkNonExistingElementInstanceOfNullElement() {
        TeasyElement el = element(By.cssSelector("not_present_element"), new SearchStrategy(1));
        Assertions.assertThat(el).isInstanceOf(NullTeasyElement.class);
        return this;
    }

    public TestElementPage checkNonExistingElementWithNullOnFailure() {
        TeasyElement el = element(By.cssSelector("not_present_element"), new SearchStrategy(1).nullOnFailure());
        Assertions.assertThat(el).isNull();
        return this;
    }

    public TestElementPage checkNonExistingElementShouldBeAbsent() {
        TeasyElement el = element(By.cssSelector("not_present_element"), new SearchStrategy(1));
        el.should().beAbsent();
        return this;
    }

    public TestElementPage checkNonExistingElementClickFails() {
        element(By.cssSelector("not_present_element"), new SearchStrategy(1)).click();
        return this;
    }

    public TestElementPage checkSingleElementHasAnyText() {
        element(By.id("exist")).should().haveAnyText();
        return this;
    }

    public TestElementPage checkFewElementsHasAnyText() {
        elements(By.className("someClassForExistElements")).should().haveAnyText();
        return this;
    }

    public TestElementPage checkSingleElementHasNotAnyText() {
        Boolean elementHasNoText = false;
        try {
            domElement(By.id("notExist")).should(new SearchStrategy(3)).haveAnyText();
        } catch (AssertionError e) {
            elementHasNoText = true;
        }
        Assertions.assertThat(elementHasNoText).isTrue();
        return this;
    }

    public TestElementPage checkNotAllElementsHasAnyText() {
        Boolean elementHasNoText = false;
        try {
            domElements(By.className("someClassForOneNotExistElements")).should(new SearchStrategy(3)).haveAnyText();
        } catch (AssertionError e) {
            elementHasNoText = true;
        }
        Assertions.assertThat(elementHasNoText).isTrue();
        return this;
    }

    public TestElementPage checkIfElementsListIsEmptyShouldThrowException() {
        elements(By.cssSelector("incorrect_locator"), new SearchStrategy(0)).should().beDisplayed();
        return this;
    }

    public TestElementPage checkDragAndDrop() {
        TeasyElement target = element(By.id("div1"));
        target.element(By.id("drag1")).should().beAbsent();
        JsActions.dragAndDrop(element(By.id("drag1")), element(By.id("div1")));
        target.element(By.id("drag1")).should().beDisplayed();
        return this;
    }
}
