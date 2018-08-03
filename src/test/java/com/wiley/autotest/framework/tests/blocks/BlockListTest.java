package com.wiley.autotest.framework.tests.blocks;

import com.wiley.autotest.framework.config.BaseUnitTest;
import com.wiley.autotest.framework.pages.BlocksPage;
import com.wiley.autotest.framework.pages.TestBlock;
import com.wiley.page.BlockList;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BlockListTest extends BaseUnitTest {

    @Test
    public void test() {
        BlocksPage blocksPage = openPage("block.html", BlocksPage.class);
        BlockList<TestBlock> blockList = blocksPage.getTestBlockList();
        Assert.assertEquals(blockList.size(), 4);

        blockList.get(3).checkText("Block from List 4");

        blockList.filter(testBlocks -> {
            for (TestBlock block : testBlocks) {
                if ("Block from List 2".equals(block.getText())) {
                    return block;
                }
            }
            return null;
        }).checkAttribute("2 item");
    }
}
