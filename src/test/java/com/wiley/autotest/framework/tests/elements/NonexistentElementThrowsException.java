package com.wiley.autotest.framework.tests.elements;

import com.wiley.autotest.framework.config.BaseTest;
import com.wiley.autotest.framework.pages.TestElementPage;
import org.testng.annotations.Test;

public class NonexistentElementThrowsException extends BaseTest {

    @Test(expectedExceptions = AssertionError.class)
    public void test() {
        openPage("mainTestElement.html", TestElementPage.class)
                .checkElementNotFound();
    }
}
