package com.wiley.autotest.selenium.elements;

import com.wiley.autotest.selenium.elements.upgrade.TeasyWebElement;
import com.wiley.autotest.selenium.elements.upgrade.TeasyWebElementData;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class TransformedWebElementList {

    private List<WebElement> webElementList;

    public TransformedWebElementList(List<WebElement> list) {
        webElementList = list;
    }

    public List<TeasyWebElement> toOurWebElementList() {
        return webElementList
                .stream().map(webElement -> new TeasyWebElement(new TeasyWebElementData(webElement)))
                .collect(Collectors.toList());
    }
}
