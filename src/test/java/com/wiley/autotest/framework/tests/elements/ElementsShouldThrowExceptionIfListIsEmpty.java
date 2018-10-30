package com.wiley.autotest.framework.tests.elements;

import com.wiley.autotest.framework.config.BaseUnitTest;
import com.wiley.autotest.framework.pages.TestElementPage;
import com.wiley.elements.NotFoundElException;
import org.testng.annotations.Test;

public class ElementsShouldThrowExceptionIfListIsEmpty extends BaseUnitTest {

    @Test(expectedExceptions = NotFoundElException.class)
    public void test() {
        openPage("mainTestElement.html", TestElementPage.class)
                .checkIfElementsListIsEmptyShouldThrowException();
    }
}
