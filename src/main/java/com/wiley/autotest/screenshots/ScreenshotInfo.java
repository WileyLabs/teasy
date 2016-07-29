package com.wiley.autotest.screenshots;

/**
 * User: dfedorov
 * Date: 5/14/12
 * Time: 3:15 PM
 */
public class ScreenshotInfo {
    private String path;
    private String message;

    public ScreenshotInfo(final String path, final String message) {
        this.message = message;
        this.path = path;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }
}
