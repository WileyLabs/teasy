package com.wiley.autotest.framework.tests.elements.custom;

import com.wiley.autotest.framework.config.BaseTest;
import com.wiley.autotest.framework.pages.SelectPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

public class SelectTests extends BaseTest {
    @Autowired
    private SelectPage selectPage;

    @Test
    public void testGetOptions() {
        openPage("select.html");
        selectPage.checkOptionsText();
    }

}
