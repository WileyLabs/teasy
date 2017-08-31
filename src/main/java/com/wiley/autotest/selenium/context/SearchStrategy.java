package com.wiley.autotest.selenium.context;

import com.wiley.autotest.selenium.SeleniumHolder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * Created by vefimov on 26/04/2017.
 */
public class SearchStrategy {

    //timeout to be used by search strategy
    private long customTimeout;
    private int poolingEvery = 0;
    private TimeUnit unit = TimeUnit.SECONDS;
    private FrameStrategy frameStrategy = FrameStrategy.FIRST_FOUND;

    //flag showing that null should be returned instead of failing
    private boolean nullOnFailure = false;

    public SearchStrategy() {
        //setting default timeout value taken from pom.xml's property 'application.selenium.timeout'
        this.customTimeout = SeleniumHolder.getTimeoutInSeconds();
    }

    public SearchStrategy(long customTimeoutInSeconds) {
        this.customTimeout = customTimeoutInSeconds;
    }

    public SearchStrategy withTimeout(long customTimeoutInSeconds) {
        this.customTimeout = customTimeoutInSeconds;
        return this;
    }

    public SearchStrategy pollingEvery(int timeInSeconds) {
        this.poolingEvery = timeInSeconds;
        return this;
    }

    public SearchStrategy pollingEvery(int time, TimeUnit unit) {
        this.poolingEvery = time;
        this.unit = unit;
        return this;
    }

    public SearchStrategy frameStrategy(FrameStrategy frameStrategy) {
        this.frameStrategy = frameStrategy;
        return this;
    }

    public SearchStrategy nullOnFailure() {
        this.nullOnFailure = true;
        return this;
    }

    public long getCustomTimeout() {
        return customTimeout;
    }

    public int getPoolingEvery() {
        return poolingEvery;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public FrameStrategy getFrameStrategy() {
        return frameStrategy;
    }

    public boolean isNullOnFailure() {
        return nullOnFailure;
    }

    public enum FrameStrategy {
        /**
         * Default value.
         * Search in all frames until there are elements.
         * Return first found elements from the first frame when found.
         */
        FIRST_FOUND,
        /**
         * Search in all frames looping through each of them.
         * Return elements from all frames.
         */
        IN_ALL_FRAMES
    }

    public enum VisibilityStrategy {

        /**
         * Return as soon as there are visible element(s)
         */
        FIRST_FOUND,

        /**
         * Return when ALL elements become visible
         */
        ALL_FOUND
    }
}
