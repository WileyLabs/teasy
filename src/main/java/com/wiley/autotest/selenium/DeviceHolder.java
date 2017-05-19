package com.wiley.autotest.selenium;

public class DeviceHolder {

    private DeviceHolder() {
    }

    private static final ThreadLocal<String> platformName = new ThreadLocal<>();

    private static final ThreadLocal<String> platformVersion = new ThreadLocal<>();

    private static final ThreadLocal<String> deviceName = new ThreadLocal<>();

    private static final ThreadLocal<String> deviceType = new ThreadLocal<>();

    private static final ThreadLocal<String> orientation = new ThreadLocal<>();

    public static void setPlatformName(String platformName) {
        DeviceHolder.platformName.set(platformName);
    }

    public static String getPlatformName() {
        return platformName.get();
    }

    public static void setPlatformVersion(String platformVersion) {
        DeviceHolder.platformVersion.set(platformVersion);
    }

    public static String getPlatformVersion() {
        return platformVersion.get();
    }

    public static void setDeviceName(String deviceName) {
        DeviceHolder.deviceName.set(deviceName);
    }

    public static String getDeviceName() {
        return deviceName.get();
    }

    public static void setDeviceType(String deviceType) {
        DeviceHolder.deviceType.set(deviceType);
    }

    public static String getDeviceType() {
        return deviceType.get();
    }

    public static void setOrientation(String orientation) {
        DeviceHolder.orientation.set(orientation);
    }

    public static String getOrientation() {
        return orientation.get();
    }
}
