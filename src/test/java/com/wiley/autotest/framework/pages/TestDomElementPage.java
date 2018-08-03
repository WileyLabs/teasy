package com.wiley.autotest.framework.pages;

import com.wiley.page.BasePage;
import com.wiley.elements.types.DomTeasyElement;
import com.wiley.elements.TeasyElement;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;

/**
 * Created by shekhavtsov on 29/09/2017.
 */
public class TestDomElementPage extends BasePage {

    public TestDomElementPage checkFirstDomElement() {
        domElement(By.cssSelector("li")).should().haveAttribute("id", "first_li_element");
        return this;
    }

    public TestDomElementPage checkDomElementBecomeVisible() {
        domElement(By.id("waitElement")).should().beDisplayed();
        return this;
    }

    /**
     * Checking that element with id "domDiv" is found.
     * The "logic" implemented in the mainTestElement.html that element will be attached to dom after 6 seconds.
     *
     * @return
     */
    public TestDomElementPage checkDomElementAppearAfterTimeout() {
        TeasyElement domDiv = domElement(By.id("domDiv"));
        Assertions.assertThat(domDiv).isInstanceOf(DomTeasyElement.class);
        domDiv.should().haveText("Im div added to dom");
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
        Assertions.assertThat(domElements(By.tagName("li")).size()).isEqualTo(5);
        return this;
    }
}
