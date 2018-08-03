package com.wiley.autotest.framework.tests.blocks;

import com.wiley.autotest.framework.config.BaseUnitTest;
import com.wiley.autotest.framework.pages.BlocksPage;
import org.testng.annotations.Test;

public class BlocksSearch extends BaseUnitTest {

    @Test
    public void test() {
        openPage("block.html", BlocksPage.class)
                .checkPageSearchesEntirePage()
                .getTestBlock()
                .checkElementIsSearchedWithinBlock();
    }
}
