package com.wiley.elements.custom;

import com.wiley.elements.TeasyElement;
import com.wiley.elements.types.TeasyElementList;

public interface Selectable {

    void selectAll();

    void selectByText(String text);

    void selectByValue(String value);

    void selectByIndex(int index);

    void selectRandom();

    TeasyElement getSelectedOption();

    String getSelectedText();

    String getSelectedValue();

    TeasyElementList getOptions();
}
