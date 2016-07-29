package com.wiley.autotest.utils;

import java.awt.*;

/**
 * Created by kamalakannan.r on 4/15/14.
 */
public final class ColorUtils {

    private ColorUtils() {
    }

    public static String getHexFromRGB(String rgba) {
        rgba = rgba.replace("rgba(", "").replace("rgb(", "").replace(")", "")
                .trim();
        String[] values = rgba.split(",");
        int red = Integer.parseInt(values[0].trim());
        int green = Integer.parseInt(values[1].trim());
        int blue = Integer.parseInt(values[2].trim());
        int alpha = 0;
        if (values.length == 4) {
            alpha = Integer.parseInt(values[3].trim());
        }
        Color c = new Color(red, green, blue, alpha);
        return "#"
                + (Integer.toHexString(c.getRGB() & 0x00ffffff)).toUpperCase();
    }
}
