package com.wiley.autotest.selenium.elements;

import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import com.wiley.autotest.selenium.elements.upgrade.TeasyElementData;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class TransformedWebElementList {

    private List<WebElement> webElementList;

    public TransformedWebElementList(List<WebElement> list) {
        webElementList = list;
    }

    public List<TeasyElement> toOurWebElementList() {
        return webElementList
                .stream().map(webElement -> new TeasyElement(new TeasyElementData(webElement)))
                .collect(Collectors.toList());
    }
}
