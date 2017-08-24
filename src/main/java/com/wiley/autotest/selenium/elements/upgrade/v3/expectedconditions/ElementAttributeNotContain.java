package com.wiley.autotest.selenium.elements.upgrade.v3.expectedconditions;

import com.wiley.autotest.selenium.elements.upgrade.OurWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;

/**
 * Created by vefimov on 24/08/2017.
 */
public class ElementAttributeNotContain  implements ExpectedCondition<Boolean>{

    private OurWebElement element;
    private String attributeName;
    private String value;

    public ElementAttributeNotContain(OurWebElement element, String attributeName, String value) {
        this.element = element;
        this.attributeName = attributeName;
        this.value = value;
    }

    @Nullable
    @Override
    public Boolean apply(@Nullable WebDriver driver) {
        return !element.getAttribute(attributeName).contains(value);
    }

    @Override
    public String toString() {
        return String.format("Element '%s' has attribute '%s' with value '%s'!", element.toString(), attributeName, value);
    }
}
