package com.wiley.autotest.framework.tests.blocks;

import com.wiley.autotest.framework.config.BaseTest;
import com.wiley.autotest.framework.pages.BlocksPage;
import org.testng.annotations.Test;

public class BlocksSearch extends BaseTest {

    @Test
    public void test() {
        openPage("block.html", BlocksPage.class)
                .checkPageSearchesEntirePage()
                .getTestBlock()
                .checkElementIsSearchedWithinBlock();
    }
}
