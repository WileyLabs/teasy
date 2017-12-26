package com.wiley.autotest.framework.tests.elements.model;

import com.wiley.autotest.framework.config.BaseTest;
import com.wiley.autotest.framework.pages.TestElementPage;
import org.openqa.selenium.NoSuchElementException;
import org.testng.annotations.Test;

/**
 * Created by vefimov on 12/09/2017.
 */
public class DifferentTypes extends BaseTest {

    @Test
    public void test() {
        openPage("mainTestElement.html");
        getPage(TestElementPage.class)
                .checkElementInstanceOfVisibleElement()
                .checkDomElementInstanceOfDomElement()
                .checkNonExistingElementInstanceOfNullElement()
                .checkNonExistingElementWithNullOnFailure()
                .checkNonExistingElementShouldBeAbsent();
    }

    @Test(expectedExceptions = {NoSuchElementException.class})
    public void nonExistingElementClickThrowException() {
        openPage("mainTestElement.html");
        getPage(TestElementPage.class)
                .checkNonExistingElementClickFails();
    }
}
