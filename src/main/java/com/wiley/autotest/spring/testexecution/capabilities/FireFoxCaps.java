package com.wiley.autotest.spring.testexecution.capabilities;

import org.openqa.selenium.Platform;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Caps for Firefox browser
 */
public class FireFoxCaps extends TeasyCaps {

    private final UnexpectedAlertBehaviour alertBehaviour;

    public FireFoxCaps(DesiredCapabilities customCaps, UnexpectedAlertBehaviour alertBehaviour) {
        super(customCaps);
        this.alertBehaviour = alertBehaviour;
    }

    public DesiredCapabilities get() {
        DesiredCapabilities caps = getFireFoxDesiredCapabilities();
        if (!this.customCaps.asMap().isEmpty()) {
            caps.merge(this.customCaps);
        }
        return caps;
    }

    private DesiredCapabilities getFireFoxDesiredCapabilities() {
        DesiredCapabilities caps = DesiredCapabilities.firefox();
        caps.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, this.alertBehaviour);
        caps.setCapability(FirefoxDriver.MARIONETTE, false);
        caps.setCapability(FirefoxDriver.PROFILE, createFirefoxProfile());
        caps.setPlatform(Platform.WINDOWS);
        caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        setLoggingPrefs(caps);
        return caps;
    }

    private FirefoxProfile createFirefoxProfile() {
        FirefoxProfile profile = new FirefoxProfile();

        profile.setPreference("dom.max_chrome_script_run_time", 999);
        profile.setPreference("dom.max_script_run_time", 999);

        //Disable plugin container. fix problem with 'FF plugin-container has stopped working'.
        profile.setPreference("dom.ipc.plugins.enabled", false);
        profile.setPreference("dom.ipc.plugins.enabled.npctrl.dll", false);
        profile.setPreference("dom.ipc.plugins.enabled.npqtplugin.dll", false);
        profile.setPreference("dom.ipc.plugins.enabled.npswf32.dll", false);
        profile.setPreference("dom.ipc.plugins.enabled.nptest.dll", false);
        profile.setPreference("dom.ipc.plugins.timeoutSecs", -1);
        //Add this to avoid JAVA plugin certificate warnings
        profile.setAcceptUntrustedCertificates(true);
        profile.setAssumeUntrustedCertificateIssuer(true);
        profile.setPreference("plugin.state.java", 2);

        //TODO VE NT check if this is still actual
        //disable  Advocacy/heartbeat in Firefox 37
        //http://selenium2.ru/news/131-rekomenduetsya-otklyuchit-advocacy-heartbeat-v-firefox-37.html
        profile.setPreference("browser.selfsupport.url", "");

        //for sso auth
        profile.setPreference("network.http.phishy-userpass-length", 255);
        profile.setPreference("network.automatic-ntlm-auth.trusted-uris", "http://,https://");

        return profile;
    }
}
