package com.wiley.assertions;

import com.wiley.holders.DriverHolder;
import com.wiley.holders.ErrorsHolder;
import com.wiley.holders.TestParamsHolder;
import com.wiley.screenshots.Screenshoter;
import org.testng.asserts.Assertion;
import org.testng.asserts.IAssert;

import java.util.LinkedList;
import java.util.List;

/**
 * User: ntyukavkin
 * Date: 11.04.2018
 * Time: 14:39
 */
public class SoftAssert extends Assertion {

    private final List<TeasyError> errors = new LinkedList<>();

    @Override
    protected void doAssert(IAssert<?> assertCommand) {
        onBeforeAssert(assertCommand);
        try {
            assertCommand.doAssert();
            onAssertSuccess(assertCommand);
        } catch (AssertionError error) {
            add(error, MethodType.TEST);
            onAssertFailure(assertCommand, error);
        }
    }

    public synchronized void add(Throwable throwable, MethodType methodType) {
        TeasyError teasyError = new TeasyError(throwable, methodType);
        if (DriverHolder.getDriver() != null) {
            String screenshotFilePath = new Screenshoter().takeScreenshot(teasyError.getErrorMessage(), TestParamsHolder.getTestName());
            teasyError.setScreenshotFilePath(screenshotFilePath);
        }
        ErrorsHolder.addError(TestParamsHolder.getTestName(), teasyError);
        errors.add(teasyError);
    }

    public void assertAll() {
        if (hasErrors()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Next ");
            sb.append(errors.size());
            sb.append(" assert");
            if (errors.size() > 1) {
                sb.append("s");
            }
            sb.append(" failed:");
            for (TeasyError e : errors) {
                sb.append("\n");
                sb.append(ordinal((errors.indexOf(e) + 1)));
                sb.append(" fail:");
                sb.append("\n\t");
                sb.append(e.getStackTraceAsString());
                sb.append("------------------------------------------------------------------------------------------------------");
            }
            errors.clear();
            throw new AssertionError(sb.toString());
        }
    }

    private boolean hasErrors() {
        return !errors.isEmpty();
    }

    private String ordinal(int i) {
        String[] suffixes = new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + suffixes[i % 10];
        }
    }
}
