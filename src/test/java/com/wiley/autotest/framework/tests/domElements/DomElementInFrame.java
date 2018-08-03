package com.wiley.autotest.framework.tests.domElements;

import com.wiley.autotest.framework.config.BaseUnitTest;
import com.wiley.autotest.framework.pages.TestDomElementPage;
import org.testng.annotations.Test;

public class DomElementInFrame extends BaseUnitTest {

    @Test
    public void test() {
        openPage("mainTestElement.html", TestDomElementPage.class)
                .checkDomElementInFrame();
    }
}
