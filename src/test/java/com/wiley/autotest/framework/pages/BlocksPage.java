package com.wiley.autotest.framework.pages;

import com.wiley.page.BasePage;
import com.wiley.page.BlockList;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;

public class BlocksPage extends BasePage {

    public TestBlock getTestBlock() {
        return new TestBlock(element(By.id("block2")));
    }

    public BlockList<TestBlock> getTestBlockList() {
        return new BlockList<>(elements(By.cssSelector(".blockFromList")), TestBlock.class);
    }

    public BlocksPage checkPageSearchesEntirePage() {
        Assertions.assertThat(elements(By.id("someDiv")).size()).isEqualTo(3);
        return this;
    }
}
