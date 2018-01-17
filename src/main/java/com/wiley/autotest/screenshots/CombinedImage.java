package com.wiley.autotest.screenshots;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author <a href="mosadchiy@wiley.com">Mikhail Osadchiy</a>
 */

/**
 * Combines three images into one
 */
class CombinedImage {

    private BufferedImage firstBufferedImage;
    private BufferedImage secondBufferedImage;
    private BufferedImage thirdBufferedImage;

    CombinedImage(BufferedImage firstBufferedImage, BufferedImage secondBufferedImage, BufferedImage thirdBufferedImage) {
        this.firstBufferedImage = firstBufferedImage;
        this.secondBufferedImage = secondBufferedImage;
        this.thirdBufferedImage = thirdBufferedImage;
    }

    BufferedImage get() {
        return combine(combine(firstBufferedImage, secondBufferedImage), thirdBufferedImage);
    }

    private BufferedImage combine(BufferedImage etalonImage, BufferedImage actualImage) {
        // create the new image, canvas size is the max. of both image sizes
        int w = etalonImage.getWidth() + actualImage.getWidth() + 1;
        int h = Math.max(etalonImage.getHeight(), actualImage.getHeight());
        BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        // paint both images, preserving the alpha channels
        Graphics g = combined.getGraphics();
        g.drawImage(etalonImage, 0, 0, null);
        g.drawImage(actualImage, etalonImage.getWidth() + 1, 0, null);
        return combined;
    }
}
