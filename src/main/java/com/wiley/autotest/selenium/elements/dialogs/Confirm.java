package com.wiley.autotest.selenium.elements.dialogs;

import com.wiley.autotest.selenium.elements.Button;
import com.wiley.autotest.selenium.elements.checkers.ConfirmChecker;

/**
 * Created by IntelliJ IDEA.
 * User: maxim
 * Date: 16.02.12
 * Time: 8:06
 */
public interface Confirm extends Alert {
    void clickTopClose();

    Button getContinue();

    Button getCancel();

    ConfirmChecker checkConfirm();
}
