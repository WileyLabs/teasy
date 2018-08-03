package com.wiley.driver.frames;

import com.google.common.base.Function;
import com.wiley.elements.TeasyElement;
import com.wiley.utils.TestUtils;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import static com.google.common.collect.Collections2.transform;
import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
import static org.testng.collections.Lists.newArrayList;

public class FramesTransparentWebDriver extends WebDriverDecorator {

    private static final Logger LOGGER = LoggerFactory.getLogger(FramesTransparentWebDriver.class);

    private final Stack<WebElement> currentFramesPath;
    private final Function<WebElement, WebElement> toFrameAwareWebElements;

    private ThreadLocal<String> mainWindowHandle = new ThreadLocal<>();
    private ThreadLocal<Boolean> firstCallInContext = ThreadLocal.withInitial(() -> true);

    public FramesTransparentWebDriver(final WebDriver driver) {
        super(driver);
        currentFramesPath = new Stack<>();
        toFrameAwareWebElements = new FrameAwareWebElementTransformer(driver, currentFramesPath);
        if (!(driver instanceof AppiumDriver)) {
            mainWindowHandle.set(driver.getWindowHandle());
        }
    }

    @Override
    public String getCurrentUrl() {
        switchToDefaultContext();
        return getDriver().getCurrentUrl();
    }

    @Override
    public List<WebElement> findElements(final By by) {
        switchToDefaultContext();
        currentFramesPath.clear();
        return findFirstElements(by);
    }

    public List<WebElement> findElements(TeasyElement context, final By by) {
        firstCallInContext.set(true);
        Stack<WebElement> currentFramesPath = new Stack<>();
        return findFirstElements(context, by, currentFramesPath);
    }

    @Override
    public WebElement findElement(final By by) {
        try {
            List<WebElement> found;

            WebElement element = driverFindElement(by);
            if (element != null) {
                found = newArrayList(transform(Collections.singletonList(element), toFrameAwareWebElements));
            } else {
                found = newArrayList(transform(driverFindElements(by), toFrameAwareWebElements));
                if (found.isEmpty()) {
                    switchToDefaultContext();
                    currentFramesPath.clear();
                    found = findFirstElements(by);
                }
            }
            return found.get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException("Unable to find element " + by + ", Exception - " + e);
        }
    }

    public WebElement findElement(TeasyElement context, final By by) {
        try {
            firstCallInContext.set(true);
            Stack<WebElement> currentFramesPath = new Stack<>();
            List<WebElement> found = findFirstElements(context, by, currentFramesPath);
            return found.get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException("Unable to find element " + by + ", Exception - " + e);
        }
    }

    public List<WebElement> findAllElementsInFrames(final By by) {
        switchToDefaultContext();
        currentFramesPath.clear();
        return getAllElementsInFrames(by);
    }

    public List<WebElement> findAllElementsInFrames(TeasyElement context, final By by) {
        Stack<WebElement> currentFramesPath = new Stack<>();
        firstCallInContext.set(true);
        return getAllElementsInFrames(context, by, currentFramesPath);
    }

    @Override
    public void close() {
        getDriver().close();
        switchToMainWindow();
    }

    @Override
    public TargetLocator switchTo() {
        return new FramesTransparentTargetLocator(getDriver().switchTo());
    }

    private void switchToDefaultContext() {
        try {
            if (!(getDriver() instanceof AppiumDriver)) {
                getDriver().switchTo().defaultContent();
            }
        } catch (WebDriverException e) {
            LOGGER.error("****WebDriverException occurs when switchToDefaultContext****", e);
            switchToMainWindow();
            getDriver().switchTo().defaultContent();
        }
    }

    private void switchToMainWindow() {
        try {
            if (!(getDriver() instanceof AppiumDriver)) {
                switchTo().window(mainWindowHandle.get());
            }
        } catch (SwitchToWindowException e) {
            LOGGER.error("****SwitchToWindowException occurs when switchToMainWindow****", e);
            switchTo().window(getDriver().getWindowHandles().iterator().next());
        }
    }

    private List<WebElement> findFirstElements(final By by) {
        final List<WebElement> foundInCurrentFrame = newArrayList(transform(driverFindElements(by), toFrameAwareWebElements));
        if (isNotEmpty(foundInCurrentFrame)) {
            return foundInCurrentFrame;
        }

        final List<WebElement> currentFrames = driverFindElements(By.tagName("iframe"));
        currentFrames.addAll(driverFindElements(By.tagName("frame")));
        for (final WebElement frame : currentFrames) {
            if (!switchToFrame(frame)) {
                continue;
            }

            currentFramesPath.push(frame);
            final List<WebElement> foundInFrames = findFirstElements(by);
            if (isNotEmpty(foundInFrames)) {
                return foundInFrames;
            }

            currentFramesPath.pop();
            getDriver().switchTo().defaultContent();
            currentFramesPath.forEach(this::switchToFrame);
        }
        return emptyList();
    }

    private List<WebElement> findFirstElements(TeasyElement context, final By by, Stack<WebElement> currentFramesPath) {
        if (firstCallInContext.get()) {

            avoidStaleness(context);

            List<WebElement> elements = context.getWrappedWebElement().findElements(by);
            if (!elements.isEmpty()) {
                return elements;
            }
        }
        if (!firstCallInContext.get()) {
            final List<WebElement> foundInCurrentFrame = newArrayList(transform(driverFindElements(by), toFrameAwareWebElements));
            if (isNotEmpty(foundInCurrentFrame)) {
                return foundInCurrentFrame;
            }
        }

        List<WebElement> currentFrames = getFramesForContext(context);
        for (final WebElement frame : currentFrames) {
            if (!switchToFrame(frame)) {
                continue;
            }

            currentFramesPath.push(frame);
            final List<WebElement> foundInFrames = findFirstElements(context, by, currentFramesPath);
            if (isNotEmpty(foundInFrames)) {
                return foundInFrames;
            }

            currentFramesPath.pop();
            getDriver().switchTo().defaultContent();
            currentFramesPath.forEach(this::switchToFrame);
        }
        return emptyList();
    }

    private List<WebElement> getAllElementsInFrames(final By by) {
        List<WebElement> foundInCurrentFrame = newArrayList(transform(driverFindElements(by), toFrameAwareWebElements));
        List<WebElement> listOfFramesInCurrentFrame = driverFindElements(By.tagName("iframe"));
        listOfFramesInCurrentFrame.addAll(driverFindElements(By.tagName("frame")));
        for (WebElement frame : listOfFramesInCurrentFrame) {
            if (!switchToFrame(frame)) {
                continue;
            }

            currentFramesPath.push(frame);

            //For resolve UnreachableBrowserException due to - java.net.SocketException: No buffer space available (maximum connections reached?): connect
            //http://stackoverflow.com/questions/1226155/hunt-down-java-net-socketexception-no-buffer-space-available
            TestUtils.sleep(50, "Wait for resolve UnreachableBrowserException, due to - SocketException: No buffer space available");
            foundInCurrentFrame.addAll(getAllElementsInFrames(by));

            currentFramesPath.pop();
            switchToDefaultContext();
            currentFramesPath.forEach(this::switchToFrame);
        }
        return foundInCurrentFrame;
    }

    private List<WebElement> getAllElementsInFrames(TeasyElement context, final By by, Stack<WebElement> currentFramesPath) {
        List<WebElement> foundInCurrentFrame = newArrayList();
        if (firstCallInContext.get()) {
            avoidStaleness(context);

            List<WebElement> elements = context.getWrappedWebElement().findElements(by);
            if (!elements.isEmpty()) {
                foundInCurrentFrame.addAll(elements);
            }
        }
        if (!firstCallInContext.get()) {
            foundInCurrentFrame.addAll(newArrayList(transform(driverFindElements(by), toFrameAwareWebElements)));
        }

        List<WebElement> currentFrames = getFramesForContext(context);
        for (WebElement frame : currentFrames) {
            if (!switchToFrame(frame)) {
                continue;
            }

            currentFramesPath.push(frame);

            //For resolve UnreachableBrowserException due to - java.net.SocketException: No buffer space available (maximum connections reached?): connect
            //http://stackoverflow.com/questions/1226155/hunt-down-java-net-socketexception-no-buffer-space-available
            TestUtils.sleep(50, "Wait for resolve UnreachableBrowserException, due to - SocketException: No buffer space available");
            foundInCurrentFrame.addAll(getAllElementsInFrames(context, by, currentFramesPath));

            currentFramesPath.pop();
            switchToDefaultContext();
            currentFramesPath.forEach(this::switchToFrame);
        }
        return foundInCurrentFrame;
    }

    /**
     * Quite hacky workaround to make sure element is not stale before searching inside it
     * we call getText() method which takes care of StaleElementReferenceException and calls
     * for againLocate() method inside TeasyElement.
     *
     * @param element - TeasyElement which might be stale.
     */
    private void avoidStaleness(TeasyElement element) {
        element.getText();
    }

    private List<WebElement> getFramesForContext(TeasyElement context) {
        List<WebElement> currentFrames;
        if (firstCallInContext.get()) {
            currentFrames = context.getWrappedWebElement().findElements(By.tagName("iframe"));
            currentFrames.addAll(context.getWrappedWebElement().findElements(By.tagName("frame")));
            firstCallInContext.set(false);
        } else {
            currentFrames = driverFindElements(By.tagName("iframe"));
            currentFrames.addAll(driverFindElements(By.tagName("frame")));
        }
        return currentFrames;
    }

    private List<WebElement> driverFindElements(By by) {
        try {
            return getDriver().findElements(by);
        } catch (NoSuchWindowException e) {
            //chrome driver throws this exception if current window is closed
            try {
                switchToMainWindow();
                return getDriver().findElements(by);
            } catch (Exception e1) {
                return new ArrayList<>();
            }
        } catch (Exception e) {
            //In some cases IE driver can throw InvalidSelectorException or NullPointerException
            return new ArrayList<>();
        }
    }

    private WebElement driverFindElement(By by) {
        try {
            return getDriver().findElement(by);
        } catch (NoSuchWindowException e) {
            //chrome driver throws this exception if current window is closed
            try {
                switchToMainWindow();
                return getDriver().findElement(by);
            } catch (Exception e1) {
                return null;
            }
        } catch (Exception e) {
            //In some cases IE driver can throw InvalidSelectorException or NullPointerException
            return null;
        }
    }

    private boolean switchToFrame(WebElement frame) {
        try {
            getDriver().switchTo().frame(frame);
        } catch (WebDriverException ignored) {
            return false;
        }
        return true;
    }

    private class FramesTransparentTargetLocator implements TargetLocator {
        private final TargetLocator targetLocator;

        public FramesTransparentTargetLocator(final TargetLocator targetLocator) {
            this.targetLocator = targetLocator;
        }

        @Override
        public WebDriver frame(final int index) {
            throw new SwitchToFrameOperationNotAllowedException();
        }

        @Override
        public WebDriver frame(final String nameOrId) {
            throw new SwitchToFrameOperationNotAllowedException();
        }

        @Override
        public WebDriver frame(final WebElement frameElement) {
            throw new SwitchToFrameOperationNotAllowedException();
        }

        @Override
        public WebDriver parentFrame() {
            throw new SwitchToFrameOperationNotAllowedException();
        }

        @Override
        public WebDriver window(final String nameOrHandle) {
            try {
                return targetLocator.window(nameOrHandle);
            } catch (NoSuchWindowException e) {
                LOGGER.error("****NoSuchWindowException occurs when get window****", e);
                if (getWindowHandles().size() == 1) {
                    return targetLocator.window(getWindowHandles().iterator().next());
                } else {
                    throw new SwitchToWindowException(format("Unable to switch to window by handler: '%s'", nameOrHandle), e);
                }
            } catch (NullPointerException e) {
                LOGGER.error("****NullPointerException occurs when get window****", e);
                throw new SwitchToWindowException("Unable to switch to window: handler is null ", e);
            }
        }

        @Override
        public WebDriver defaultContent() {
            return targetLocator.defaultContent();
        }

        @Override
        public WebElement activeElement() {
            return targetLocator.activeElement();
        }

        @Override
        public Alert alert() {
            return targetLocator.alert();
        }
    }
}
