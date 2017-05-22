package com.wiley.autotest.selenium.elements.upgrade.v3;

import java.util.concurrent.TimeUnit;

/**
 * Created by vefimov on 26/04/2017.
 */
public class SearchStrategy {

    public int timeoutInSeconds;
    public int poolingEvery = 0;
    public TimeUnit unit = TimeUnit.SECONDS;
    public FrameStrategy frameStrategy;

    //flag showing that null should be returned instead of failing
    public boolean nullOnFailure = false;

    public SearchStrategy withTimeout(int timeoutInSeconds) {
        this.timeoutInSeconds = timeoutInSeconds;
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

    public SearchStrategy nullOnFailure() {
        this.nullOnFailure = true;
        return this;
    }

    public int getTimeout() {
        return timeoutInSeconds;
    }

    public int getPoolingEvery() {
        return poolingEvery;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public enum FrameStrategy {
        /**
         * Default value.
         * Search in all frames until there are elements.
         * Return first found elements from the first frame when found.
         */
        FIRST_ELEMENTS,
        /**
         * Search in all frames looping through each of them.
         * Return elements from all frames.
         */
        IN_ALL_FRAMES
    }
}
