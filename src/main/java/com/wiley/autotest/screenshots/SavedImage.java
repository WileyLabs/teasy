package com.wiley.autotest.screenshots;

import org.jetbrains.annotations.NotNull;
import org.testng.Assert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author <a href="mosadchiy@wiley.com">Mikhail Osadchiy</a>
 */

/**
 * Writes image into *.png file by path and name
 */
public class SavedImage {

    private BufferedImage bufferedImage;
    private String folderPath;
    private String name;

    public SavedImage(BufferedImage bufferedImage, String folderPath, String name) {
        this.bufferedImage = bufferedImage;
        this.folderPath = folderPath;
        this.name = name;
    }

    public void toFile() {
        if (!asFile().exists()) {
            asFile().mkdirs();
        }
        try {
            ImageIO.write(bufferedImage, "png", asFile());
        } catch (IOException e) {
            Assert.fail("IOException occurred during saving image to file", e);
        }
    }

    @NotNull
    public File asFile() {
        return new File(folderPath, name + ".png");
    }
}
