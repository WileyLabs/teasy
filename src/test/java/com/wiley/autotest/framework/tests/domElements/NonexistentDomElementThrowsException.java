package com.wiley.autotest.framework.tests.domElements;

import com.wiley.autotest.framework.config.BaseTest;
import com.wiley.autotest.framework.pages.TestDomElementPage;
import org.testng.annotations.Test;

public class NonexistentDomElementThrowsException extends BaseTest {

    @Test(expectedExceptions = AssertionError.class)
    public void test() {
        openPage("mainTestElement.html");
        getPage(TestDomElementPage.class)
                .checkDomElementNotFound();
    }
}
