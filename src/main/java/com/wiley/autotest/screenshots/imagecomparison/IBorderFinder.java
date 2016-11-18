package com.wiley.autotest.screenshots.imagecomparison;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collection;

/**
 * User: vefimov
 * Date: 20.06.13
 * Time: 14:25
 */
public interface IBorderFinder {

    Collection<IContour> find(BufferedImage image);

    Collection<Rectangle> unionRectangles(Collection<Rectangle> rectangles, final int accuracy);
}
