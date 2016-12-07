package com.wiley.autotest.event.postpone.failure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.Reporter;

import static java.lang.String.format;

public class StorePostponeFailureSubscriber extends PostponeFailureSubscriber {

    private final String testName;
    private final ITestContext context;
    private static final Logger LOGGER = LoggerFactory.getLogger(StorePostponeFailureSubscriber.class);

    public StorePostponeFailureSubscriber(final ITestContext context, String testName) {
        this.testName = testName;
        this.context = context;
    }

    @Override
    public void notify(final String message) {
        Reporter.log(message);
        LOGGER.error(message);
        context.setAttribute(format("%s postpone fail", testName), true);
    }
}
