package com.wiley.autotest.screenshots;

import java.util.*;

/**
 * User: dfedorov
 * Date: 5/11/12
 * Time: 9:08 AM
 */
public final class ScreenShotsPathsHolder {
    private static Map<String, List<ScreenshotInfo>> pathsHolder = new HashMap<String, List<ScreenshotInfo>>();

    private ScreenShotsPathsHolder() {
    }

    public static void addScreenShotPathForTest(final String testName, final String path, final String message) {
        List<ScreenshotInfo> screenshotsInfo;
        if (pathsHolder.containsKey(testName)) {
            screenshotsInfo = pathsHolder.get(testName);
        } else {
            screenshotsInfo = new ArrayList<ScreenshotInfo>();
            pathsHolder.put(testName, screenshotsInfo);
        }

        screenshotsInfo.add(new ScreenshotInfo(path, message));
    }

    public static List<ScreenshotInfo> getScreenShotPathsForTest(final String testName) {
        return pathsHolder.containsKey(testName) ? pathsHolder.get(testName) : Collections.<ScreenshotInfo>emptyList();
    }
}
