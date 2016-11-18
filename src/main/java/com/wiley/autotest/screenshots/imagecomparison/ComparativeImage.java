package com.wiley.autotest.screenshots.imagecomparison;

import java.awt.image.BufferedImage;

/**
 * User: vefimov
 * Date: 20.06.13
 * Time: 14:27
 */
public class ComparativeImage {

    private double pixelContradictions;

    private double areaContradictions;

    public void setMaskImage(final BufferedImage maskImage) {
        this.maskImage = maskImage;
    }

    private BufferedImage maskImage;

    public void setPixelContradictions(final double pixelContradictions) {
        this.pixelContradictions = pixelContradictions;
    }

    public double getPixelContradictions() {
        return pixelContradictions;
    }

    public BufferedImage getMaskImage() {
        return maskImage;
    }

    public void setAreaContradictions(final double areaContradictions) {
        this.areaContradictions = areaContradictions;
    }

    public double getAreaContradictions() {
        return areaContradictions;
    }
}
