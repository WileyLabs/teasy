package com.wiley.autotest.selenium.elements.upgrade;

import com.wiley.autotest.selenium.elements.upgrade.v3.conditions.window.WindowMatcher;

/**
 * Created by vefimov on 30/05/2017.
 */
public interface Window {

     void switchToLast();
     void close();

     //Todo implement commot waitFor and should (similar to OurWebElement) but with limited functionality only required for window
    void waitForScriptsToLoad();

    void switchTo(WindowMatcher matcher);


}
