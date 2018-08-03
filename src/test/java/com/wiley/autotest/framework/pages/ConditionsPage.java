package com.wiley.autotest.framework.pages;

import com.wiley.page.BasePage;
import com.wiley.elements.SearchStrategy;
import com.wiley.elements.types.TeasyElementList;
import org.openqa.selenium.By;

import java.util.Arrays;
import java.util.List;

/**
 * Created by shekhavtsov on 29/09/2017.
 */
public class ConditionsPage extends BasePage {

    public ConditionsPage checkElementsReturnAllVisibleElements() {
        TeasyElementList teasyElements = domElements(By.cssSelector(".test"));
        teasyElements.should().beDisplayed();
        List<String> expectedTexts = Arrays.asList("Div1", "Div2", "Div3", "Div4", "Div5");
        teasyElements.should().haveTexts(expectedTexts);

        return this;
    }

    public ConditionsPage callConditionForEmptyList() {
        TeasyElementList els = elements(By.id("notExistingId"), new SearchStrategy(1));
        els.should().haveText("no matter what text is here");

        return this;
    }

    public ConditionsPage callAbsentConditionForEmptyList() {
        TeasyElementList els = elements(By.id("notExistingId"), new SearchStrategy(1));
        els.should().beAbsent();

        return this;
    }
}
