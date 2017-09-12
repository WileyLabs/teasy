package com.wiley.autotest.framework.tests.elements.model;

import com.wiley.autotest.framework.config.BaseTest;
import com.wiley.autotest.framework.pages.TestElementPage;
import org.openqa.selenium.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * Created by vefimov on 12/09/2017.
 */
public class DifferentTypes extends BaseTest {

    @Autowired
    private TestElementPage testElementPage;

    @Test
    public void test() {
        openPage("mainTestElement.html");
        testElementPage
                .checkElementInstanceOfVisibleElement()
                .checkDomElementInstanceOfDomElement()
                .checkNonExistingElementInstanceOfNullElement()
                .checkNonExistingElementShouldBeAbsent();
    }

    @Test(expectedExceptions = {NoSuchElementException.class})
    public void nonExistingElementClickThrowException() {
        openPage("mainTestElement.html");
        testElementPage
                .checkNonExistingElementClickFails();
    }
}
