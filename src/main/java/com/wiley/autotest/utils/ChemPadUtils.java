package com.wiley.autotest.utils;

/**
 * User: ptsarev
 * Date: 29.06.2016
 */
//Will be deleted. If you need this class - copy it to your project.
@Deprecated
public final class ChemPadUtils {

    /**
     * private constructor for utils class
     */
    private ChemPadUtils() {
    }

    public static String getChemPadValueForChrome(String chemPadValue) {
        String[] chemPadValueArr = chemPadValue.split("");
        return String.join("\n", chemPadValueArr);
    }
}
