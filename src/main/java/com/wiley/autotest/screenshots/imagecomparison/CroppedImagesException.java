package com.wiley.autotest.screenshots.imagecomparison;

import java.awt.image.BufferedImage;

/**
 * Created by mosadchiy on 26.05.2016.
 */

public class CroppedImagesException extends Exception {

    private BufferedImage image1;
    private BufferedImage image2;

    CroppedImagesException(final BufferedImage image1, final BufferedImage image2) {
        super();
        this.image1 = image1;
        this.image2 = image2;
    }

    BufferedImage getImage1() {
        return image1;
    }

    BufferedImage getImage2() {
        return image2;
    }
}