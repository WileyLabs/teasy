package com.wiley.autotest.selenium.elements.checkers;

import com.wiley.autotest.selenium.context.ErrorSender;
import com.wiley.autotest.selenium.elements.Element;
import org.testng.Reporter;

import static com.wiley.autotest.utils.StringUtils.deleteHyphens;

/**
 * Created by IntelliJ IDEA.
 * User: maxim
 * Date: 22.02.12
 * Time: 15:19
 */
public class Checker<E extends Element> {
    private final ErrorSender errorSender;
    private final E element;

    public ErrorSender getErrorSender() {
        return errorSender;
    }

    public E getElement() {
        return element;
    }

    public Checker(final E element, final ErrorSender errorSender) {
        this.errorSender = errorSender;
        this.element = element;
    }

    public void text(final String nameOfTheTab) throws AssertionError {
        if (!textByNoThrow(nameOfTheTab)) {
            final String errorMessage = "ERROR: " + element.getClass().getSimpleName() + " contains '" + element.getText() +
                    "' text instead of '" + nameOfTheTab + "'";
            Reporter.log(errorMessage);
            throw new AssertionError(errorMessage);
        }
    }

    public void textByPostponedFail(final String nameOfTheTab) throws AssertionError {
        if (errorSender != null) {
            if (!textByNoThrow(nameOfTheTab)) {
                final String errorMessage = "ERROR: " + element.getClass().getSimpleName() + " contains '" + element.getText() +
                        "' text instead of '" + nameOfTheTab + "'";
                errorSender.setPostponedTestFail(errorMessage);
            }
        } else {
            text(nameOfTheTab);
        }
    }

    public boolean textByNoThrow(final String nameOfTheTab) {
        return nameOfTheTab.equalsIgnoreCase(deleteHyphens(element.getText()));
    }
}
