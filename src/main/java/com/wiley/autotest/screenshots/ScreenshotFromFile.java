package com.wiley.autotest.screenshots;

import org.jetbrains.annotations.NotNull;
import org.testng.Assert;
import ru.yandex.qatools.ashot.Screenshot;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * Created by mosadchiy on 23.08.2017.
 */
public class ScreenshotFromFile {

    private String folderPath;
    private String name;

    public ScreenshotFromFile(String folderPath, String name) {
        this.folderPath = folderPath;
        this.name = name;
    }

    public boolean exists() {
        return asFile().exists();
    }

    public Screenshot read() {
        try {
            return new Screenshot(ImageIO.read(asFile()));
        } catch (IOException e) {
            Assert.fail("IOException occurred during reading image from file", e);
        }
        return null;
    }

    public String fullPath() {
        return asFile().getPath();
    }

    @NotNull
    private File asFile() {
        return new File(folderPath, name + ".png");
    }
}
