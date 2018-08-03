package com.wiley.elements.conditions.window;

public class WindowMatcher {

    private WindowFinder finder;

    private WindowMatcher(WindowFinder finder) {
        this.finder = finder;
    }

    public WindowFinder get() {
        return finder;
    }

    public static WindowMatcher byTitle(String title) {
        return new WindowMatcher(new WindowByTitle(title));
    }

    public static WindowMatcher byUrl(String url) {
        return new WindowMatcher(new WindowByUrl(url));
    }

    public static WindowMatcher byPartialUrl(String urlPart) {
        return new WindowMatcher(new WindowByPartialUrl(urlPart));
    }

}
