package com.wiley.autotest.selenium.context;

import com.wiley.autotest.actions.Actions;
import com.wiley.autotest.actions.Conditions;
import com.wiley.autotest.actions.RepeatableAction;
import com.wiley.autotest.selenium.Report;
import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.selenium.elements.upgrade.Window;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.testng.Reporter;
import ru.yandex.qatools.allure.annotations.Step;

import static com.wiley.autotest.utils.DateUtils.waitForAssignmentDate;
import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * User: dfedorov
 * Date: 1/5/12
 * Time: 12:34 PM
 */
public abstract class AbstractPage<P extends AbstractPage> extends AbstractPageElement<P> implements IPage {

    private int count = 0;

    private final String path;

    public AbstractPage() {
        path = null;
    }

    public void load() {
        load(path);
    }

    public void load(final String pathString) {
        if (isNotBlank(pathString)) {
            getDriver().get(pathString);
        }
    }

    /**
     * This method has to be added in every test that fails because of bug
     * This method has to be added right before the method which fails because of bug
     * If after this method test does not fail, it probably means that the bug was fixed
     * and this method call has to be removed as well as bug annotation and group from the test
     *
     * @param bugId - id of a bug,
     *              in case when there's no bug but system behavior is different from test case
     *              ask for an approval of manual QA team; If they say "OK, keep it as is" use
     *              EXPECTED_FAILURE_AGREED_WITH_MANUAL_QA constant as an ID.
     * @return this page
     */
    @Step
    public P bugInNextStepReportAlert(String bugId) {
        new Report("The next step will fail because of bug with id '" + bugId + "'!").allure();
        return (P) this;
    }

    @Step
    public P bugInNextStepReportAlert() {
        String bugId = SeleniumHolder.getBugId();
        if (bugId != null) {
            new Report("The next step will fail because of bug with id '" + bugId + "'!").allure();
        }
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

    /**
     * use {@link Report#allure()}
     */
    @Deprecated
    protected final void log(final String message) {
        Reporter.log(message);
    }

    /**
     * use {@link Report#allure()}
     */
    @Deprecated
    protected final void log(final String format, final Object... args) {
        log(String.format(format, args));
    }

    /**
     * use {@link Window#close()}
     */
    @Deprecated
    @Step
    public <P extends AbstractPage> P closeCurrentWindow(final Class<P> target) {
        closeBrowserWindow();
        return redirectTo(target);
    }

    /**
     * use {@link Window#close()} {@link Window#switchToLast()}
     */
    @Deprecated
    @Step
    public <P extends AbstractPage> P closeCurrentWindowAndSwitchToLastWindow(final Class<P> target) {
        closeBrowserWindow();
        switchToLastWindow();
        return redirectTo(target);
    }

    /**
     * {@link Window#changeSize(int, int)}
     */
    @Deprecated
    @Step
    public P setBrowserDimensions(int width, int height) {
        Dimension dimension = new Dimension(width, height);
        getDriver().manage().window().setSize(dimension);
        return (P) this;
    }

    //Copy this to your project if you use it. The method will be deleted
    @Deprecated
    public static By getLinkByXpath(String linkText) {
        return By.xpath("//a[text()='" + linkText + "']");
    }

    //Copy this to your project if you use it. The method will be deleted
    @Deprecated
    @Step
    public P waitForDate(DateTimeZone dateTimeZone, DateTime dueDate) {
        waitForAssignmentDate(dateTimeZone, dueDate);
        return (P) this;
    }

    //Copy this to your project if you use it. The method will be deleted
    @Deprecated
    @Step
    public <T extends AbstractPage> T waitForDate(DateTimeZone dateTimeZone, DateTime dueDate, Class<T> target) {
        waitForAssignmentDate(dateTimeZone, dueDate);
        return redirectTo(target);
    }

    //Copy this to your project if you use it. The method will be deleted
    @Step
    @Deprecated
    public P checkTitleOfBrowserWindow(String expectedTitle) {
        postponedAssertEquals(getDriver().getTitle(), expectedTitle, "Incorrect title of browser window");
        return (P) this;
    }

    @Deprecated // move to your project. constant will be deleted
    public static final By TABLE_LOCATOR = By.tagName("table");
    @Deprecated // move to your project. constant will be deleted
    public static final By TR_LOCATOR = By.tagName("tr");
    @Deprecated // move to your project. constant will be deleted
    public static final By TD_LOCATOR = By.tagName("td");
    @Deprecated // move to your project. constant will be deleted
    public static final By TH_LOCATOR = By.tagName("th");
    @Deprecated // move to your project. constant will be deleted
    public static final By SELECT_LOCATOR = By.tagName("select");
    @Deprecated // move to your project. constant will be deleted
    public static final By SPAN_LOCATOR = By.tagName("span");
    @Deprecated // move to your project. constant will be deleted
    public static final By DIV_LOCATOR = By.tagName("div");
    @Deprecated // move to your project. constant will be deleted
    public static final By P_LOCATOR = By.tagName("p");
    @Deprecated // move to your project. constant will be deleted
    public static final By A_LOCATOR = By.tagName("a");
    @Deprecated // move to your project. constant will be deleted
    public static final By B_LOCATOR = By.tagName("b");
    @Deprecated // move to your project. constant will be deleted
    public static final By INPUT_LOCATOR = By.tagName("input");
    @Deprecated // move to your project. constant will be deleted
    public static final By IMG_LOCATOR = By.tagName("img");
    @Deprecated // move to your project. constant will be deleted
    protected static final String CLASS_ATTRIBUTE = "class";
}
