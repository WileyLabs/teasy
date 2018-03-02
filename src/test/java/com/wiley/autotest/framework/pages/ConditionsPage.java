package com.wiley.autotest.framework.pages;

import com.wiley.autotest.selenium.context.AbstractPage;
import com.wiley.autotest.selenium.elements.upgrade.TeasyElementList;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created by shekhavtsov on 29/09/2017.
 */
@Component
public class ConditionsPage extends AbstractPage {

    public ConditionsPage checkElementsReturnAllVisibleElements() {
        TeasyElementList teasyElements = domElements(By.cssSelector(".test"));
        teasyElements.should().beDisplayed();
        List<String> expectedTexts = Arrays.asList("Div1", "Div2", "Div3", "Div4", "Div5");
        teasyElements.should().haveTexts(expectedTexts);

        return this;
    }
}
