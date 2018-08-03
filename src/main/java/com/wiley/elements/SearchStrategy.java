package com.wiley.elements;

import com.wiley.config.Configuration;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

/**
 * Created by vefimov on 26/04/2017.
 */
public class SearchStrategy {

    //timeout to be used by search strategy
    private long customTimeout;
    private static final long DEFAULT_SLEEP_TIMEOUT = 500L;
    private long poolingEvery = DEFAULT_SLEEP_TIMEOUT;
    private TemporalUnit unit = ChronoUnit.MILLIS;
    private FrameStrategy frameStrategy = FrameStrategy.FIRST_FOUND;

    //flag showing that null should be returned instead of failing
    private boolean nullOnFailure = false;

    public SearchStrategy() {
        //setting default timeout value taken from pom.xml's property 'application.selenium.timeout'
        this(Configuration.timeout);
    }

    public SearchStrategy(long customTimeoutInSeconds) {
        this.customTimeout = customTimeoutInSeconds;
    }

    public SearchStrategy withTimeout(long customTimeoutInSeconds) {
        this.customTimeout = customTimeoutInSeconds;
        return this;
    }

    public SearchStrategy pollingEvery(long time, TemporalUnit unit) {
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

    public long getPoolingEvery() {
        return poolingEvery;
    }

    public TemporalUnit getUnit() {
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
