package com.wiley.autotest.selenium.elements.upgrade;

import com.wiley.autotest.selenium.elements.upgrade.v3.conditions.window.WindowMatcher;

/**
 * Created by vefimov on 30/05/2017.
 */
public interface Window {

    void switchToLast();

    void switchTo(WindowMatcher matcher);

    void close();

    //Todo implement common waitFor and should (similar to TeasyElement) but with limited functionality only required for window
    void waitForScriptsToLoad();

    String getUrl();


}
