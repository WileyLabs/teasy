package com.wiley.elements.find;

import com.wiley.elements.*;
import com.wiley.elements.conditions.FramesConditionFactory;
import com.wiley.elements.types.TeasyElementList;
import com.wiley.elements.types.TeasyElementType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Finding elements in DOM
 */
public class DomElementsLookUp implements LookUpWithLocator<TeasyElementList> {
    private final TeasyElement context;
    private final TeasyFluentWait<WebDriver> fluentWait;
    private final FramesConditionFactory conditionFactory;

    public DomElementsLookUp(WebDriver driver, SearchStrategy strategy) {
        this(driver, strategy, null);
    }

    public DomElementsLookUp(WebDriver driver, SearchStrategy strategy, TeasyElement context) {
        this.fluentWait = new TeasyFluentWait<>(driver, strategy);
        this.context = context;
        this.conditionFactory = new FramesConditionFactory(context, strategy.getFrameStrategy());
    }

    @Override
    public TeasyElementList find(By locator) {
        List<WebElement> webElements;
        try {
            webElements = fluentWait.waitFor(conditionFactory.get().presenceOfList(locator));
        } catch (AssertionError ignoredToReturnEmptyList) {
            webElements = null;
        }

        TeasyElementList elements;
        if (webElements == null) {
            elements = new TeasyElementList(new ArrayList<>(), locator);
        } else {
            List<TeasyElement> list = new ArrayList<>(webElements.size());
            for (int i = 0; i < webElements.size(); i++) {
                list.add(ElementWrapper.wrap(new TeasyElementData(webElements.get(i), locator, context, i), TeasyElementType.DOM));
            }
            elements = new TeasyElementList(list, locator);
        }

        return elements;
    }
}
