package com.wiley.autotest.selenium.extensions.internal;

import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.UnhandledAlertException;

class TextField extends AbstractElement {
    protected TextField(final TeasyElement el) {
        super(el);
    }

    public void type(final String value) {
        try {
            getWrappedElement().sendKeys(value);
        } catch (UnhandledAlertException ignored) {
            //In case we try to input some non text/number symbol like ")" or "+" selenium throws this exception
            //this fails the test (for example E4_1920); The solution is to ignore this exception
            //If this causes some problems in future this try catch should be refactored

            //ignored.printStackTrace();
        }
    }

    private void typeWithoutCheck(final String value) {
        //make text field focused (for ie)
        getWrappedElement().click();
        try {
            getWrappedElement().sendKeys(value);
        } catch (UnhandledAlertException ignored) {
            //In case we try to input some non text/number symbol like ")" or "+" seleniuk throws this exception
            //this fails the test (for example E4_1920); The solution is to ignore this exception
            //If this causes some problems in future this try catch should be refactored

            //ignored.printStackTrace();
        }
    }

    public void clear() {
        getWrappedElement().clear();
    }

    public void clearWithBackspaceAndType(String toType) {
        int numberOfCharactersToDelete = getWrappedElement().getAttribute("value")
                .toCharArray().length;
        while (numberOfCharactersToDelete > 0) {
            getWrappedElement().sendKeys(Keys.BACK_SPACE);
            numberOfCharactersToDelete--;
        }
        getWrappedElement().sendKeys(toType);
    }

    public void clearWithBackspaceAndTypeWithTab(String toType) {
        clearWithBackspaceAndType(toType);
        // Tab key is sent to make sure the keypress/change/blur event gets triggered...
        getWrappedElement().sendKeys(Keys.TAB);
    }

    public String getText() {
        return getWrappedElement().getAttribute("value");
    }

    public void clearAndType(final String value) {
        clear();
        type(value);
    }

    /**
     * Use the method for checking an alert after incorrect data input in the text field
     *
     * @param value
     */
    public void clearAndTypeWithoutCheck(final String value) {
        clear();
        typeWithoutCheck(value);
    }
}
