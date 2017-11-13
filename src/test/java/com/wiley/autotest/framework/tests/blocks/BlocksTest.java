package com.wiley.autotest.framework.tests.blocks;

import com.wiley.autotest.framework.config.BaseTest;
import com.wiley.autotest.framework.pages.BlocksPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

public class BlocksTest extends BaseTest {

    @Autowired
    private BlocksPage blocksPage;

    @Test
    public void test() {
        openPage("block.html");
        blocksPage.
                checkPageSearchesEntirePage()
                .getTestBlock().checkElementIsSearchedWithinBlock();
    }
}
