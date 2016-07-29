package com.wiley.autotest.selenium.elements;

public interface TextField extends EnabledElement {
    void type(final String value);

    void clear();

    void clearAndType(final String value);

    void clearAndTypeWithoutCheck(String value);

    void clearWithBackspaceAndType(String toType);

    void clearWithBackspaceAndTypeWithTab(String toType);
}
