package com.wiley.autotest.screenshots.imagecomparison;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.awt.Color.BLACK;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.lang.Math.*;

/**
 * User: vefimov
 * Date: 20.06.13
 * Time: 14:27
 */
public class ImageMerger implements IImageMerger {

    private static final int DIFF_BRIGHTNESS_THRESHOLD = 2;
    private static final int DIFF_COLOR_THRESHOLD = 2;

    @Override
    public ComparativeImage compare(final BufferedImage image1, final BufferedImage image2) throws ImageSizeException {
        BufferedImage croppedImage1 = image1, croppedImage2 = image2;
        try {
            checkImageWidth(croppedImage1, croppedImage2);
        } catch (CroppedImagesException e) {
            croppedImage1 = e.getImage1();
            croppedImage2 = e.getImage2();
        }
        try {
            checkImageHeight(croppedImage1, croppedImage2);
        } catch (CroppedImagesException e) {
            croppedImage1 = e.getImage1();
            croppedImage2 = e.getImage2();
        }
        return calcContradictions(croppedImage1, croppedImage2);
    }

    private void checkImageWidth(final BufferedImage image1, final BufferedImage image2) throws ImageSizeException, CroppedImagesException {
        String error = null;
        if (image1.getWidth() != image2.getWidth()) {
            error = "Images' widths do not match: " + image1.getWidth() + " != " + image2.getWidth() + ".";
            if (image1.getWidth() > image2.getWidth()) {
                if (needImageCropping(image1, getControlledRectangleByWidth(image1, image2), image2, getControllingRectangleByWidth(image2))) {
                    throw new CroppedImagesException(image1.getSubimage(0, 0, image2.getWidth(), image1.getHeight()), image2);
                }
            } else {
                if (needImageCropping(image2, getControlledRectangleByWidth(image2, image1), image1, getControllingRectangleByWidth(image1))) {
                    throw new CroppedImagesException(image1, image2.getSubimage(0, 0, image1.getWidth(), image2.getHeight()));
                }
            }
        }
        if (error != null) {
            throw new ImageSizeException(error);
        }
    }

    private void checkImageHeight(final BufferedImage image1, final BufferedImage image2) throws ImageSizeException, CroppedImagesException {
        String error = null;
        if (image1.getHeight() != image2.getHeight()) {
            error = "Images' heights do not match: " + image1.getHeight() + " != " + image2.getHeight() + ".";
            if (image1.getHeight() > image2.getHeight()) {
                if (needImageCropping(image1, getControlledRectangleByHeight(image1, image2), image2, getControllingRectangleByHeight(image2))) {
                    throw new CroppedImagesException(image1.getSubimage(0, 0, image1.getWidth(), image2.getHeight()), image2);
                }
            } else {
                if (needImageCropping(image2, getControlledRectangleByHeight(image2, image1), image1, getControllingRectangleByHeight(image1))) {
                    throw new CroppedImagesException(image1, image2.getSubimage(0, 0, image2.getWidth(), image1.getHeight()));
                }
            }
        }
        if (error != null) {
            throw new ImageSizeException(error);
        }
    }

    private Rectangle getControlledRectangleByWidth(final BufferedImage image1, final BufferedImage image2) {
        return new Rectangle(image2.getWidth(), 0, image1.getWidth() - image2.getWidth(), image1.getHeight());
    }

    private Rectangle getControlledRectangleByHeight(final BufferedImage image1, final BufferedImage image2) {
        return new Rectangle(0, image2.getHeight(), image1.getWidth(), image1.getHeight() - image2.getHeight());
    }

    private Rectangle getControllingRectangleByWidth(final BufferedImage image) {
        return new Rectangle(image.getWidth() - 1, 0, 1, image.getHeight());
    }

    private Rectangle getControllingRectangleByHeight(final BufferedImage image) {
        return new Rectangle(0, image.getHeight() - 1, image.getWidth(), 1);
    }

    private boolean needImageCropping(final BufferedImage image1, final Rectangle rectangle1, final BufferedImage image2, final Rectangle rectangle2) {
        int[] imagePixels1 = image1.getRGB(rectangle1.x, rectangle1.y, rectangle1.width, rectangle1.height, null, 0, rectangle1.width);
        int[] imagePixels2 = image2.getRGB(rectangle2.x, rectangle2.y, rectangle2.width, rectangle2.height, null, 0, rectangle2.width);
        int[] imagePixels = new int[imagePixels1.length + imagePixels2.length];
        System.arraycopy(imagePixels1, 0, imagePixels, 0, imagePixels1.length);
        System.arraycopy(imagePixels2, 0, imagePixels, imagePixels1.length, imagePixels2.length);
        for (int i = 1; i < imagePixels.length; i++) {
            if (imagePixels[i - 1] != imagePixels[i]) {
                return false;
            }
        }
        return true;
    }

    private ComparativeImage calcContradictions(final BufferedImage image1, final BufferedImage image2) {
        final int width = image1.getWidth();
        final int height = image1.getHeight();
        final ComparativeImage result = new ComparativeImage();
        final BufferedImage maskImage = emptyImage(width, height);
        int differentPixelsCount = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (abs(image1.getRGB(x, y) - image2.getRGB(x, y)) > 0) {
                    final Color firstPointColor = new Color(image1.getRGB(x, y));
                    final Color secondPointColor = new Color(image2.getRGB(x, y));
                    final int diffRed = abs(firstPointColor.getRed() - secondPointColor.getRed());
                    final int diffGreen = abs(firstPointColor.getGreen() - secondPointColor.getGreen());
                    final int diffBlue = abs(firstPointColor.getBlue() - secondPointColor.getBlue());
                    final double brightDistance = sqrt(pow(diffRed, 2) + pow(diffGreen, 2) + pow(diffBlue, 2));
                    final double rgDistance = sqrt(pow(diffRed, 2) + pow(diffGreen, 2));
                    final double gbDistance = sqrt(pow(diffGreen, 2) + pow(diffBlue, 2));
                    final double rbDistance = sqrt(pow(diffRed, 2) + pow(diffBlue, 2));
                    final double colorDistance = (rgDistance + gbDistance + rbDistance) / 3.;
                    if (brightDistance > DIFF_BRIGHTNESS_THRESHOLD || colorDistance > DIFF_COLOR_THRESHOLD) {
                        differentPixelsCount++;
                        maskImage.setRGB(x, y, BLACK.getRGB());
                    }
                }
            }
        }

        double pixelContradictions = 0.0;
        double areaContradictions = 0.0;
        if (differentPixelsCount > 0) {
            final int imgArea = width * height;
            pixelContradictions = ((double) differentPixelsCount / imgArea) * 100;
            areaContradictions = (MergeUtils.getDiffArea(maskImage) / imgArea) * 100;
        }

        result.setPixelContradictions(pixelContradictions);
        result.setAreaContradictions(areaContradictions);
        result.setMaskImage(maskImage);
        return result;
    }

    private BufferedImage emptyImage(final int width, final int height) {
        final BufferedImage maskImage = new BufferedImage(width, height, TYPE_INT_RGB);
        final Graphics2D graphics = maskImage.createGraphics();
        graphics.setBackground(Color.WHITE);
        graphics.clearRect(0, 0, width, height);
        return maskImage;
    }
}
