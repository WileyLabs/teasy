package com.wiley.autotest.screenshots.imagecomparison;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import static java.awt.Color.BLACK;

/**
 * User: vefimov
 * Date: 20.06.13
 * Time: 14:26
 */
public class BorderFinder implements IBorderFinder {

    private static final int BOUNDS = 5;

    @Override
    public Collection<IContour> find(final BufferedImage image) {
        final ArrayList<IContour> contours = new ArrayList<IContour>();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (marked(x, y, image)) {
                    final int x1 = x;
                    final int y1 = y;
                    final Point startAt = new Point(x1, y1);
                    if (!containsInAnyContour(startAt, contours)) {
                        linkPoint(startAt, contours);
                    }
                }
            }
        }
        return contours;
    }

    @Override
    public Collection<Rectangle> unionRectangles(final Collection<Rectangle> rectangles, final int accuracy) {
        final Collection<Rectangle> resultCollection = new ArrayList<Rectangle>();

        final Vector<Rectangle> tmpVector = new Vector<Rectangle>(rectangles);
        for (int idx1 = 0; idx1 < tmpVector.size(); idx1++) {
            final Rectangle firstItem = tmpVector.get(idx1);
            for (int idx2 = 0; idx2 < tmpVector.size(); idx2++) {
                if (idx1 != idx2) {
                    final Rectangle secondItem = tmpVector.get(idx2);
                    firstItem.grow(accuracy, accuracy);
                    secondItem.grow(accuracy, accuracy);
                    final boolean intersects = firstItem.intersects(secondItem);
                    firstItem.grow(-accuracy, -accuracy);
                    secondItem.grow(-accuracy, -accuracy);
                    if (intersects) {
                        final Rectangle result = firstItem.union(secondItem);
                        firstItem.setBounds(result);
                        tmpVector.remove(idx2);
                        idx2 = 0;
                    }
                }
            }
            resultCollection.add(firstItem);
        }
        return resultCollection;
    }

    private void linkPoint(final Point point, final ArrayList<IContour> contours) {
        for (final IContour contour : contours) {
            final Rectangle contourRect = contour.getRectangle();
            final Rectangle extendedRect = new Rectangle(contourRect.x - BOUNDS, contourRect.y - BOUNDS, contourRect.width + BOUNDS * 2, contourRect.height + BOUNDS * 2);
            if (extendedRect.contains(point)) {
                contour.add(point);
                return;
            }
        }
        contours.add(new Contour(point));
    }

    private boolean containsInAnyContour(final Point point, final ArrayList<IContour> contours) {
        for (final IContour contour : contours) {
            if (contour.getRectangle().contains(point)) {
                return true;
            }
        }
        return false;
    }

    private boolean marked(final int x, final int y, final BufferedImage image) {
        return x >= 0 && y >= 0 && x < image.getWidth() && y < image.getHeight() && image.getRGB(x, y) == BLACK.getRGB();
    }
}
