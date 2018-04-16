package com.wiley.autotest.selenium.context;

import com.wiley.autotest.actions.Actions;
import com.wiley.autotest.actions.Conditions;
import com.wiley.autotest.actions.RepeatableAction;
import com.wiley.autotest.selenium.Report;
import com.wiley.autotest.selenium.SeleniumHolder;
import io.qameta.allure.Step;

import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * User: dfedorov
 * Date: 1/5/12
 * Time: 12:34 PM
 */
public abstract class AbstractPage<P extends AbstractPage> extends AbstractPageElement<P> implements IPage {

    public AbstractPage() {
    }

    public void load(final String pathString) {
        if (isNotBlank(pathString)) {
            SeleniumHolder.getWebDriver().get(pathString);
        }
    }

    /**
     * This method has to be added in every test that fails because of bug
     * This method has to be added right before the method which fails because of bug
     * If after this method test does not fail, it probably means that the bug was fixed
     * and this method call has to be removed as well as bug annotation and group from the test
     *
     * @param bugId - id of a bug
     * @return current page
     */
    @Step
    public P bugInNextStepReportAlert(String bugId) {
        new Report("The next step will fail because of bug with id '" + bugId + "'!").allure();
        return (P) this;
    }

    /**
     * Performs an action until a condition is true.
     * <p>
     * example:
     * action(element(By.cssSelector("a"))::click, element(By.cssSelector("div"))::isDisplayed);
     *
     * @param action    - any action to perform
     * @param condition - condition to make after action
     * @return current page
     */
    public P action(Actions action, Conditions condition) {
        new RepeatableAction(action, condition).perform();
        return (P) this;
    }

    public P action(Actions action, Conditions condition, int numberOfAttempts, int millisecondsBetweenAttempts) {
        new RepeatableAction(action, condition, numberOfAttempts, millisecondsBetweenAttempts).perform();
        return (P) this;
    }
}
