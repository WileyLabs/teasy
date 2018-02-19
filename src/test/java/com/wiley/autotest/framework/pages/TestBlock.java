package com.wiley.autotest.framework.pages;

import com.wiley.autotest.selenium.context.AbstractBlock;
import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import org.openqa.selenium.By;
import org.testng.Assert;

public class TestBlock extends AbstractBlock {

    public TestBlock(TeasyElement element) {
        super(element);
    }

    public TestBlock checkElementIsSearchedWithinBlock() {
        Assert.assertEquals(elements(By.id("someDiv")).size(), 1);
        element(By.id("someDiv")).should().haveText("Im a div in block 2");
        return this;
    }

    public TestBlock checkText(String text) {
        getMainElement().should().haveText(text);
        return this;
    }

    public String getText() {
        return getMainElement().getText();
    }

    public TestBlock checkAttribute(String value) {
        getMainElement().should().haveAttribute("customAttribute", value);
        return this;
    }
}
