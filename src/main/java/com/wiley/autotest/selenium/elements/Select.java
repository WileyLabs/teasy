package com.wiley.autotest.selenium.elements;

import com.wiley.autotest.selenium.elements.upgrade.OurWebElement;
import org.openqa.selenium.WebElement;

import java.util.List;

public interface Select extends Element, EnabledElement {
    void selectByText(String key);

    // Since some filters cannot be used with 'isListLoaded()' when Preview Assignment button is clicked,
    // the new method is used to do that without wait
    void selectByTextWithoutWait(String key);

    void selectByIndex(int index);

    void selectByValue(String value);

    void selectByText(String key, String errorMessage);

    void selectByIndex(int index, String errorMessage);

    void selectByValue(String value, String errorMessage);

    void selectByAnotherTextThan(String text);

    void selectRandom();

    void selectRandomByAnotherTextThan(String text);

    void selectByPartialText(String partialText);

    OurWebElement getSelectedOption();

    String getSelectedText();

    String getSelectedValue();

    List<OurWebElement> getOptions();

    List<OurWebElement> getValues();

    int getSelectedIndex();

    void deselectAll();

    void selectAll();
}
