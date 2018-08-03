package com.wiley.autotest.framework.pages;

import com.wiley.page.AbstractBlock;
import com.wiley.elements.TeasyElement;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;

public class TestBlock extends AbstractBlock {

    public TestBlock(TeasyElement element) {
        super(element);
    }

    public TestBlock checkElementIsSearchedWithinBlock() {
        Assertions.assertThat(elements(By.id("someDiv")).size()).isEqualTo(1);
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
