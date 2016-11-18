package com.wiley.autotest.screenshots.imagecomparison;

import java.awt.*;

/**
 * User: vefimov
 * Date: 20.06.13
 * Time: 14:26
 */
public interface IContour {

    Rectangle getRectangle();

    void add(Point startAt);
}
