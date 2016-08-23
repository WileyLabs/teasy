package com.wiley.autotest.screenshots.imagecomparison;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.wiley.autotest.screenshots.Screenshoter;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

import static java.io.File.separator;
import static java.lang.String.format;
import static javax.imageio.ImageIO.write;

/**
 * User: vefimov
 * Date: 20.06.13
 * Time: 14:28
 */
public class MergeUtils {

    private static final int ACCURACY = 10;
    private static final int WIDTH = 2;

    public static void writeDiff(final String directory, final String name, final BufferedImage actualImage, final ComparativeImage comparativeImage) {
        final String pathName = directory + separator + name;
        if (comparativeImage != null) {
            writeDiff(actualImage, comparativeImage);

            writePng(comparativeImage.getMaskImage(), pathName + "_mask");
        }
        writePng(actualImage, pathName);
    }

    public static void writeDiff(final String directory, final String name, final BufferedImage etalonImage, final BufferedImage actualImage, final ComparativeImage comparativeImage) {
        final String pathName = directory + separator + name;

        if (comparativeImage != null) {
            writeDiff(actualImage, comparativeImage);
        }

        writePng(joinBufferedImage(etalonImage, actualImage), pathName);
    }

    private static void writeDiff(final BufferedImage actualImage, final ComparativeImage comparativeImage) {
        final Graphics2D graphics = actualImage.createGraphics();
        final BufferedImage maskImage = comparativeImage.getMaskImage();

        final Collection<Rectangle> unionRectangles = getUnionRectangles(maskImage);
        for (final Rectangle rectangle : unionRectangles) {
            graphics.setColor(Color.RED);
            graphics.setStroke(new BasicStroke(WIDTH));
            int offset = (WIDTH >> 1) + 1;
            graphics.drawRect(rectangle.x - offset, rectangle.y - offset, rectangle.width + (offset << 1), rectangle.height + (offset << 1));
        }
    }

    private static BufferedImage joinBufferedImage(BufferedImage etalonImage, BufferedImage actualImage) {
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

    private static Collection<Rectangle> getUnionRectangles(final BufferedImage maskImage) {
        final BorderFinder borderFinder = new BorderFinder();

        final Collection<IContour> contours = borderFinder.find(maskImage);

        final Collection<Rectangle> rectangles = Collections2.transform(contours, new Function<IContour, Rectangle>() {
            @Override
            @Nullable
            public Rectangle apply(@Nullable final IContour contour) {
                assert contour != null;
                return contour.getRectangle();

            }
        });

        return borderFinder.unionRectangles(rectangles, ACCURACY);
    }

    public static double getDiffArea(final BufferedImage maskImage) {
        double diffArea = 0.0;
        for (final Rectangle rectangle : getUnionRectangles(maskImage)) {
            diffArea += rectangle.getHeight() * rectangle.getWidth();
        }

        return diffArea;
    }

    public static void writePng(final BufferedImage image, final String pathName) {
        try {
            File screenshot = new File(format("%s.png", pathName));
            write(image, "PNG", screenshot);
            new Screenshoter().attachScreenShotToAllure("Comparative screenshot", "", screenshot);
        } catch (IOException ignored) {
        }
    }
}
