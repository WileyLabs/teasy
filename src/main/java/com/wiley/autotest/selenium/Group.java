package com.wiley.autotest.selenium;

public final class Group {

    //Groups related to test plans
    public static final String smoke = "smoke";
    public static final String core = "core";
    public static final String regression = "regression";
    public static final String developers = "developers";
    public static final String switchBackOffice = "switchBackOffice";

    //Groups related to tests readiness state
    public static final String implemented = "implemented";
    public static final String inDevelopment = "inDevelopment";
    public static final String inTesting = "inTesting";

    //Groups related to cross browser tests execution
    public static final String noSafari = "noSafari";
    public static final String noIE = "noIE";
    public static final String noFF = "noFF";
    public static final String noChrome = "noChrome";
    public static final String noAndroid = "noAndroid";
    public static final String noIos = "noIos";
    public static final String noMac = "noMac";
    public static final String noWindows = "noWindows";

    /**
     * tests that fails because of bugs are marked with this group
     * basically such tests are excluded from nightly builds
     */
    public static final String bug = "bug";

    private Group() {
    }
}
