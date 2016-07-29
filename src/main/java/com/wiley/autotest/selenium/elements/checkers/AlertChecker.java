package com.wiley.autotest.selenium.elements.checkers;

import com.wiley.autotest.selenium.context.ErrorSender;
import com.wiley.autotest.selenium.elements.dialogs.Alert;

/**
 * Created by IntelliJ IDEA.
 * User: maxim
 * Date: 16.02.12
 * Time: 9:57
 */
public class AlertChecker extends Checker<Alert> {

    public AlertChecker(final Alert alert, final ErrorSender errorSender) {
        super(alert, errorSender);
    }
}
