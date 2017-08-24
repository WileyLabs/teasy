package com.wiley.autotest.screenshots;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by mosadchiy on 23.08.2017.
 */
public class ComparativeScreenshot {

    private static final Log LOGGER = LogFactory.getLog(ComparativeScreenshot.class);
    private String folderPath;
    private String name;

    public ComparativeScreenshot(String folderPath, String name) {
        this.folderPath = folderPath;
        this.name = name;
    }

    public boolean compare(Screenshot reference, Screenshot actual) {
        if (!actual.getIgnoredAreas().isEmpty()) {
            reference.setIgnoredAreas(actual.getIgnoredAreas());
        }
        ImageDiff diff = new ImageDiffer().makeDiff(reference, actual);
        if (diff.hasDiff()) {
            BufferedImage outputImage = new CombinedImage(reference.getImage(), diff.getMarkedImage(), actual.getImage()).get();
            SavedImage savedImage = new SavedImage(outputImage, folderPath, name);
            savedImage.toFile();
            try {
                new Screenshoter().attachScreenShotToAllure("Comparative screenshot", "", savedImage.asFile());
            } catch (IOException e) {
                LOGGER.error("IOException occurred during attaching comparative screenshot to allure", e);
            }
            return false;
        }
        return true;
    }
}