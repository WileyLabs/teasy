package com.wiley.autotest.framework.tests.context;

import com.wiley.autotest.framework.config.BaseTest;
import com.wiley.autotest.framework.pages.ContextSearchPage;
import org.testng.annotations.Test;

/**
 * Created by shekhavtsov on 20/07/2017.
 */
public class ContextSearch extends BaseTest {

    @Test
    public void test() {
        openPage("contextSearch.html", ContextSearchPage.class)
                .checkContextSearch();
    }
}
