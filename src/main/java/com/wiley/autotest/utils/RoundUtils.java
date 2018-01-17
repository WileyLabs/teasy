package com.wiley.autotest.utils;

import java.math.BigDecimal;

/**
 * User: vefimov
 * Date: 03.07.13
 * Time: 16:15
 */
//Will be deleted. If you need this class - copy it to your project.
@Deprecated
public final class RoundUtils {

    private static final double MIN_DIFFERENCE = 0.011;

    /**
     * private constructor for utils class
     */
    private RoundUtils() {
    }

    public static double getRoundDouble(double localDouble) {
        if (new BigDecimal(String.valueOf(localDouble)).scale() > 2) {
            return new BigDecimal(localDouble).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
        } else {
            return localDouble;
        }
    }

    public static boolean compareDoubleAndString(Double doubleValue, String stringValue) {
        return Math.abs(Double.valueOf(stringValue) - doubleValue) < MIN_DIFFERENCE;
    }

    public static boolean compareDoubles(Double doubleFirst, Double doubleSecond) {
        return Math.abs(doubleFirst - doubleSecond) < MIN_DIFFERENCE;
    }
}
