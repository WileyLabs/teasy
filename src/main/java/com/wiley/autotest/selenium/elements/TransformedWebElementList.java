package com.wiley.autotest.selenium.elements;

import com.wiley.autotest.selenium.elements.upgrade.OurWebElement;
import com.wiley.autotest.selenium.elements.upgrade.OurWebElementData;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class TransformedWebElementList {

    private List<WebElement> webElementList;

    public TransformedWebElementList(List<WebElement> list) {
        webElementList = list;
    }

    public List<OurWebElement> toOurWebElementList() {
        return webElementList
                .stream().map(webElement -> new OurWebElement(new OurWebElementData(webElement)))
                .collect(Collectors.toList());
    }
}
