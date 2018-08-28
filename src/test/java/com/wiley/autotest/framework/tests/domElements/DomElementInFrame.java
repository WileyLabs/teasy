package com.wiley.autotest.framework.tests.domElements;

import com.wiley.autotest.framework.config.BaseTest;
import com.wiley.autotest.framework.pages.TestDomElementPage;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

public class DomElementInFrame extends BaseTest {

    @Test
    public void test() {

        Assertions.fail("Sdfsdfsdf");
        openPage("mainTestElement.html", TestDomElementPage.class)
                .checkDomElementInFrame();
    }
}
