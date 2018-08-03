package com.wiley.autotest.framework.tests.elements;

import com.wiley.autotest.framework.config.BaseUnitTest;
import com.wiley.autotest.framework.pages.TestElementPage;
import org.openqa.selenium.NoSuchElementException;
import org.testng.annotations.Test;

public class ElementsShouldThrowExceptionIfListIsEmpty extends BaseUnitTest {

    @Test(expectedExceptions = NoSuchElementException.class)
    public void test() {
        openPage("mainTestElement.html", TestElementPage.class)
                .checkIfElementsListIsEmptyShouldThrowException();
    }
}
