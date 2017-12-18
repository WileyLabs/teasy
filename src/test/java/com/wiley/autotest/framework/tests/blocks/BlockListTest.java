package com.wiley.autotest.framework.tests.blocks;

import com.wiley.autotest.framework.config.BaseTest;
import com.wiley.autotest.framework.pages.BlocksPage;
import com.wiley.autotest.framework.pages.TestBlock;
import com.wiley.autotest.selenium.context.blocklist2.BlockList;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.is;

public class BlockListTest extends BaseTest {

    @Autowired
    private BlocksPage blocksPage;

    @Test
    public void test() {
        openPage("block.html");
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
        }).checkAttribute("item 2");
    }
}
