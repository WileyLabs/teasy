package com.wiley.autotest.framework.pages;

import com.wiley.autotest.selenium.context.AbstractPage;
import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SelectPage extends AbstractPage {

    public SelectPage checkOptionsText() {
        List<TeasyElement> options = select(By.tagName("select")).getOptions();
        assertEquals(options.size(), 4);
        options.get(0).should().haveText("Volvo");
        options.get(1).should().haveText("Saab");
        options.get(2).should().haveText("Mercedes");
        options.get(3).should().haveText("Audi");
        return this;
    }
}
