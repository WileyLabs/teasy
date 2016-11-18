package com.wiley.autotest.screenshots.imagecomparison;

import java.awt.*;

/**
 * User: vefimov
 * Date: 20.06.13
 * Time: 14:27
 */
public class Contour implements IContour {

    private int x;
    private int y;
    private int width;
    private int height;
    private Rectangle rectangle;

    public Contour(final Point startAt) {
        x = (int) startAt.getX();
        y = (int) startAt.getY();
        add(startAt);
    }

    @Override
    public void add(final Point shiftPoint) {
        if (shiftPoint.getX() < x) {
            width += x - shiftPoint.getX();
            x = (int) shiftPoint.getX();
        } else if (shiftPoint.getX() > x + width) {
            width += shiftPoint.getX() - width - x;
        }
        if (shiftPoint.getY() < y) {
            height += y - shiftPoint.getY();
            y = (int) shiftPoint.getY();
        } else if (shiftPoint.getY() > y + height) {
            height += shiftPoint.getY() - height - y;
        }
        rectangle = new Rectangle(x - 1, y - 1, width + 2, height + 2);
    }

    @Override
    public Rectangle getRectangle() {
        return rectangle;
    }

    @Override
    public String toString() {
        return String.format("Contour with rectangle[%s]", getRectangle());
    }
}
