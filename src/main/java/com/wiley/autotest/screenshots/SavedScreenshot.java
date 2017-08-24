package com.wiley.autotest.screenshots;

import org.jetbrains.annotations.NotNull;
import org.testng.Assert;
import ru.yandex.qatools.ashot.Screenshot;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * Created by mosadchiy on 22.08.2017.
 */
public class SavedScreenshot {

    private Screenshot screenshot;
    private String folderPath;
    private String name;

    public SavedScreenshot(Screenshot screenshot, String folderPath, String name) {
        this.screenshot = screenshot;
        this.folderPath = folderPath;
        this.name = name;
    }

    public void toFile() {
        if (!asFile().exists()) {
            asFile().mkdirs();
        }
        try {
            ImageIO.write(screenshot.getImage(), "png", asFile());
        } catch (IOException e) {
            Assert.fail("IOException occurred during saving image to file", e);
        }
    }

    @NotNull
    public File asFile() {
        return new File(folderPath, name + ".png");
    }
}
