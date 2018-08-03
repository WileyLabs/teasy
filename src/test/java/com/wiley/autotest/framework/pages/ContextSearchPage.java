package com.wiley.autotest.framework.pages;

import com.wiley.elements.TeasyElement;
import com.wiley.page.BasePage;
import org.openqa.selenium.By;
import org.testng.Assert;

import java.util.List;

public class ContextSearchPage extends BasePage {

    public ContextSearchPage checkContextSearch() {
        List<TeasyElement> elements = element(By.cssSelector(".test")).elements(By.cssSelector(".contextTest"));
        Assert.assertEquals(elements.size(), 1);
        elements.get(0).should().haveText("Hello context");
        return this;
    }
}
