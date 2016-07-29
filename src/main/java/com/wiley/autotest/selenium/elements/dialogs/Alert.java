package com.wiley.autotest.selenium.elements.dialogs;

import com.wiley.autotest.selenium.elements.WebContainer;
import com.wiley.autotest.selenium.elements.checkers.AlertChecker;

/**
 * Created by IntelliJ IDEA.
 * User: maxim
 * Date: 16.02.12
 * Time: 8:06
 */
public interface Alert extends WebContainer {
    void clickOk();

    void clickClose();

    String getTitle();

    String getIcon();

    AlertChecker checkAlert();
}
