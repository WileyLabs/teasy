package com.wiley.elements;

import com.wiley.elements.conditions.window.WindowMatcher;

import java.net.URL;

/**
 * Represents Browser Window and all possible actions in it
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

    void scrollTo(TeasyElement element);

    void back();

    void forward();

    void refresh();

    void navigateTo(String url);

    void navigateTo(URL url);
}
