package com.wiley.autotest.framework.pages;

import com.wiley.autotest.selenium.context.AbstractPage;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

@Component
public class BlocksPage extends AbstractPage<BlocksPage> {

    public TestBlock getTestBlock() {
        return new TestBlock(element(By.id("block2")));
    }

    public BlocksPage checkPageSearchesEntirePage() {
        assertEquals(elements(By.id("someDiv")).size(), 3);
        return this;
    }
}
