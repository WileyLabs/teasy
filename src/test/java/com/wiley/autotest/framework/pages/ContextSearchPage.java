package com.wiley.autotest.framework.pages;

import com.wiley.autotest.selenium.context.AbstractPage;
import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContextSearchPage extends AbstractPage {

    public ContextSearchPage checkContextSearch() {
        List<TeasyElement> elements = element(By.cssSelector(".test")).elements(By.cssSelector(".contextTest"));
        assertEquals(elements.size(), 1);
        elements.get(0).should().haveText("Hello context");
        return this;
    }
}
