package com.wiley.autotest.framework.tests.context;

import com.wiley.autotest.framework.config.BaseTest;
import com.wiley.autotest.framework.pages.ContextSearchPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * Created by shekhavtsov on 20/07/2017.
 */
public class ContextSearch extends BaseTest {

    @Autowired
    private ContextSearchPage contextSearchPage;

    @Test
    public void test() {
        openPage("contextSearch.html");
        contextSearchPage.checkContextSearch();
    }

}
