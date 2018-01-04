package com.wiley.autotest.utils;

/**
 * Created by mosadchiy on 26.05.2016.
 */
//Will be deleted. If you need this class - copy it to your project.
@Deprecated
public class FontUtils {

    private static final double PX_TO_REM_FACTOR = 1. / 14.;

    public static String convertPx2Rem(String fontSize) {
        if (fontSize.contains("px")) {
            float px = Float.valueOf(fontSize.substring(0, fontSize.length() - 2));
            return (Math.round(px * PX_TO_REM_FACTOR * 1000) / 1000.) + "rem";
        }
        return fontSize;
    }

}