package com.wiley.driver.frames;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.HasIdentity;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Stack;

import static com.google.common.collect.Collections2.transform;
import static java.lang.reflect.Proxy.newProxyInstance;
import static org.apache.commons.collections.CollectionUtils.isEqualCollection;
import static org.testng.collections.Lists.newArrayList;

/**
 * This class is used to replace original WebElement behavior
 * to ease work with element from different frames.
 */
public class FrameAwareWebElementTransformer implements Function<WebElement, WebElement> {

    private final Stack<WebElement> currentFramesPath;
    private final WebDriver driver;

    public FrameAwareWebElementTransformer(final WebDriver driver, final Stack<WebElement> currentFramesPath) {
        this.driver = driver;
        this.currentFramesPath = currentFramesPath;
    }

    @Override
    public WebElement apply(final WebElement element) {
        return (WebElement) newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{WebElement.class, WrapsElement.class, Locatable.class, HasIdentity.class},
                invocationHandlerFor(element)
        );
    }

    private InvocationHandler invocationHandlerFor(final WebElement element) {
        return new FrameAwareWebElementMethodInvocationHandler(element, driver, currentFramesPath);
    }

    private static class FrameAwareWebElementMethodInvocationHandler implements InvocationHandler {
        private final WebElement element;
        private final WebDriver driver;
        private final List<WebElement> framesPathToElement;
        private final Stack<WebElement> currentFramesPath;

        public FrameAwareWebElementMethodInvocationHandler(final WebElement element,
                                                           final WebDriver driver,
                                                           final Stack<WebElement> currentFramesPath) {
            this.element = element;
            this.driver = driver;
            this.currentFramesPath = currentFramesPath;
            this.framesPathToElement = ImmutableList.<WebElement>builder()
                    .addAll(currentFramesPath)
                    .build();
        }

        @Override
        public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
            currentFramesPathClearAndPush();
            final String methodName = method.getName();
            if (methodName.equals("getWrappedElement")) {
                return element;
            }
            if (methodName.equals("findElement")) {
                final WebElement found = (WebElement) method.invoke(element, args);
                return transformerToFramesAwareWebElement().apply(found);
            }

            if (methodName.equals("findElements")) {
                final List<WebElement> found = (List<WebElement>) method.invoke(element, args);
                return newArrayList(transform(found, transformerToFramesAwareWebElement()));
            }
            try {
                return method.invoke(element, args);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        }

        private void currentFramesPathClearAndPush() {
            if (!isEqualCollection(framesPathToElement, currentFramesPath)) {
                driver.switchTo().defaultContent();
                currentFramesPath.clear();

                for (final WebElement each : framesPathToElement) {
                    driver.switchTo().frame(each);
                    currentFramesPath.push(each);
                }
            }
        }

        private FrameAwareWebElementTransformer transformerToFramesAwareWebElement() {
            return new FrameAwareWebElementTransformer(driver, currentFramesPath);
        }
    }
}
