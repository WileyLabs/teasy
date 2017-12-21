package com.wiley.autotest.framework.tests.elements;

import com.wiley.autotest.framework.config.BaseTest;
import com.wiley.autotest.framework.pages.TestElementPage;
import org.openqa.selenium.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;


public class NonexistentElementThrowsException extends BaseTest {

    @Autowired
    private TestElementPage testElementPage;

    @Test(expectedExceptions = AssertionError.class)
    public void test() {
        openPage("mainTestElement.html");
        testElementPage.checkElementNotFound();
    }
}
