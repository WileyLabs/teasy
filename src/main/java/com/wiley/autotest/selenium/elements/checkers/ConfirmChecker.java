package com.wiley.autotest.selenium.elements.checkers;

import com.wiley.autotest.selenium.context.ErrorSender;
import com.wiley.autotest.selenium.elements.Button;
import com.wiley.autotest.selenium.elements.dialogs.Confirm;
import org.testng.Reporter;

import static com.wiley.autotest.utils.StringUtils.deleteHyphens;

/**
 * Created by IntelliJ IDEA.
 * User: maxim
 * Date: 16.02.12
 * Time: 9:57
 */
public class ConfirmChecker extends Checker<Confirm> {

    public ConfirmChecker(final Confirm confirm, final ErrorSender errorSender) {
        super(confirm, errorSender);
    }

    private void textInButton(final Button button, final String buttonText) throws AssertionError {
        if (!textByNoThrow(buttonText)) {
            final String errorMessage = "ERROR: " + button.getClass().getSimpleName() + " contains '" + button.getText() +
                    "' text instead of '" + buttonText + "'";
            Reporter.log(errorMessage);
            throw new AssertionError(errorMessage);
        }
    }

    private void textInButtonByPostponedFail(final Button button, final String buttonText) throws AssertionError {
        if (getErrorSender() != null) {
            if (!textInButtonByNoThrow(button, buttonText)) {
                final String errorMessage = "ERROR: " + button.getClass().getSimpleName() + " contains '" + getElement().getText() +
                        "' text instead of '" + buttonText + "'";
                getErrorSender().setPostponedTestFail(errorMessage);
            }
        } else {
            textInButton(button, buttonText);
        }
    }

    private boolean textInButtonByNoThrow(final Button button, final String nameOfTheTab) {
        return nameOfTheTab.equalsIgnoreCase(deleteHyphens(button.getText()));
    }

    public void textInSaveButton(final String buttonText) throws AssertionError {
        textInButton(getElement().getContinue(), buttonText);
    }

    public void textInCancelButton(final String buttonText) throws AssertionError {
        textInButton(getElement().getCancel(), buttonText);
    }

    public void textInSaveButtonByPostponedFail(final String buttonText) throws AssertionError {
        textInButtonByPostponedFail(getElement().getContinue(), buttonText);
    }

    public void textInCancelButtonByPostponedFail(final String buttonText) throws AssertionError {
        textInButtonByPostponedFail(getElement().getCancel(), buttonText);
    }
}
