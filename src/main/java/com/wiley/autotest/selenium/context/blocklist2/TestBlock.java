package com.wiley.autotest.selenium.context.blocklist2;

import com.wiley.autotest.selenium.context.AbstractBlock;
import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;

public class TestBlock extends AbstractBlock {
    public TestBlock(TeasyElement element) {
        super(element);
    }

    public TestBlock clickSometh() {
        return this;
    }
}
