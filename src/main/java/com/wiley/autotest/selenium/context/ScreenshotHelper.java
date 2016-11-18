package com.wiley.autotest.selenium.context;

import com.wiley.autotest.selenium.driver.events.listeners.ScreenshotWebDriverEventListener;

/**
 * Created by mosadchiy on 26.05.2016.
 */

public interface ScreenshotHelper {

    int MAX_FOLDER_NAME_LENGTH = 224;

    ScreenshotWebDriverEventListener getScreenshotWebDriverEventListener();

    String getScreenshotPath();

    String getComparativePath();

    int nextPass();
}
