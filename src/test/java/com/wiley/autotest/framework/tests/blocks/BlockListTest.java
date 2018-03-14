package com.wiley.autotest.framework.tests.blocks;

import com.wiley.autotest.framework.config.BaseTest;
import com.wiley.autotest.framework.pages.BlocksPage;
import com.wiley.autotest.framework.pages.TestBlock;
import com.wiley.autotest.selenium.context.BlockList;
import org.junit.Assert;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.is;

public class BlockListTest extends BaseTest {

    @Test
    public void test() {
        BlocksPage blocksPage = openPage("block.html", BlocksPage.class);
        BlockList<TestBlock> blockList = blocksPage.getTestBlockList();
        Assert.assertThat(blockList.size(), is(4));

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
