package com.wiley.autotest.selenium.elements.upgrade;

import com.wiley.autotest.selenium.elements.upgrade.conditions.window.WindowMatcher;

import java.net.URL;

/**
 * Represents Browser Window and all possible actions of it
 */
public interface Window {

    void switchToLast();

    void switchTo(WindowMatcher matcher);

    void close();

    void waitForScriptsToLoad();

    String getUrl();

    void changeSize(int width, int height);

    String getTitle();

    void maximize();

    void back();

    void forward();

    void refresh();

    void navigateTo(String url);

    void navigateTo(URL url);
}
