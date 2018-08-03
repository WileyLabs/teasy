package com.wiley.holders;

import com.wiley.assertions.SoftAssert;

/**
 * A holder to store information about Soft Assert
 */
public class AssertionsHolder {

    private static final ThreadLocal<SoftAssert> softAssertHolder = new ThreadLocal<>();

    public static SoftAssert softAssert() {
        return softAssertHolder.get();
    }

    public static void setSoftAssert(SoftAssert softAssert) {
        AssertionsHolder.softAssertHolder.set(softAssert);
    }
}
