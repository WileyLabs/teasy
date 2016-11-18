package com.wiley.autotest.screenshots.imagecomparison;

import java.awt.image.BufferedImage;

/**
 * User: vefimov
 * Date: 20.06.13
 * Time: 14:26
 */
public interface IImageMerger {

    ComparativeImage compare(BufferedImage image1, BufferedImage image2) throws ImageSizeException;
}
