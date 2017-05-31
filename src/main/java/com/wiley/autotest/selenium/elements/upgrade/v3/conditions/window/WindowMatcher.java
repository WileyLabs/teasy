package com.wiley.autotest.selenium.elements.upgrade.v3.conditions.window;

/**
 * Created by vefimov on 31/05/2017.
 */
public class WindowMatcher {

    private WindowCondition condition;

    private WindowMatcher(WindowCondition condition) {
        this.condition = condition;
    }

    public WindowCondition get() {
        return condition;
    }

    public static WindowMatcher byTitle(String title) {
        return new WindowMatcher(new WindowByTitle(title));
    }

    public static WindowMatcher byUrl(String url) {
        return new WindowMatcher(new WindowByUrl(url));
    }

}
