package com.wiley.autotest.framework.tests.elements.custom;

import com.wiley.autotest.framework.config.BaseTest;
import com.wiley.autotest.framework.pages.SelectPage;
import org.testng.annotations.Test;

public class SelectTests extends BaseTest {

    @Test
    public void testGetOptions() {
        openPage("select.html", SelectPage.class)
                .checkOptionsText();
    }
}
