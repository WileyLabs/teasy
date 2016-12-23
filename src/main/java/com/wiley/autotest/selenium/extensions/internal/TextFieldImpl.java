package com.wiley.autotest.selenium.extensions.internal;

import com.wiley.autotest.selenium.elements.TextField;
import com.wiley.autotest.utils.TestUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.HasInputDevices;

import static com.wiley.autotest.utils.ExecutionUtils.isAndroid;
import static com.wiley.autotest.utils.ExecutionUtils.isIE;
import static com.wiley.autotest.utils.ExecutionUtils.isSafari;

class TextFieldImpl extends AbstractEnabledElement implements TextField {
    protected TextFieldImpl(final WebElement wrappedElement) {
        super(wrappedElement);
    }

    protected TextFieldImpl(final WebElement wrappedElement, By by) {
        super(wrappedElement, by);
    }

    @Override
    public void type(final String value) {
        //make text field focused (for ie)
        if (isIE() || isSafari()) {
            try {
                getWrappedElement().click();
            } catch (WebDriverException ignored) {
            }
        }
        try {
            getWrappedElement().sendKeys(value);

            //In IE sometimes
            if (isIE() && !getText().equals(value)) {
                TestUtils.waitForSomeTime(1000, EXPLANATION_MESSAGE_FOR_WAIT);
                clear();
                getWrappedElement().sendKeys(value);
            }

        } catch (UnhandledAlertException ignored) {
            //In case we try to input some non text/number symbol like ")" or "+" selenium throws this exception
            //this fails the test (for example E4_1920); The solution is to ignore this exception
            //If this causes some problems in future this try catch should be refactored

            //ignored.printStackTrace();
        }
        if (isAndroid()) {
            //For hide keyboard
            ((HasInputDevices) getDriver()).getKeyboard().sendKeys(Keys.TAB);
            ((JavascriptExecutor) getDriver()).executeScript("document.body.style.transform='scale(1)'");
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

    @Override
    public void clear() {
        getWrappedElement().clear();
    }

    public void clearWithBackspaceAndType(String toType) {
        int numberOfCharactersToDelete = getWrappedWebElement().getAttribute("value").toCharArray().length;
        while (numberOfCharactersToDelete > 0) {
            getWrappedWebElement().sendKeys(Keys.BACK_SPACE);
            numberOfCharactersToDelete--;
        }
        getWrappedWebElement().sendKeys(toType);
    }

    public void clearWithBackspaceAndTypeWithTab(String toType) {
        clearWithBackspaceAndType(toType);
        // Tab key is sent to make sure the keypress/change/blur event gets triggered...
        getWrappedWebElement().sendKeys(Keys.TAB);
    }

    @Override
    public String getText() {
        return getWrappedElement().getAttribute("value");
    }

    @Override
    public WebElement getWrappedWebElement() {
        return getWrappedElement();
    }

    @Override
    public void clearAndType(final String value) {
        clear();
        type(value);
    }

    /**
     * Use the method for checking an alert after incorrect data input in the text field
     *
     * @param value
     */
    @Override
    public void clearAndTypeWithoutCheck(final String value) {
        clear();
        typeWithoutCheck(value);
    }
}
