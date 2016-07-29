package com.wiley.autotest.screenshots;

import com.wiley.autotest.annotations.InsertElement;
import com.wiley.autotest.annotations.InsertElements;
import com.wiley.autotest.utils.JavaUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mosadchiy on 26.05.2016.
 */

public class ScreenshotLocator {
    public By locator;

    public int marginTop = 0;
    public int marginRight = 0;
    public int marginLeft = 0;
    public int marginBottom = 0;

    public Rectangle getRectangle(WebElement element) {
        Point location = element.getLocation();
        Dimension dimension = element.getSize();
        return new Rectangle(location.x - marginLeft, location.y - marginTop,
                dimension.width + marginLeft + marginRight, dimension.height + marginTop + marginBottom);
    }

    public static ScreenshotLocator id(String id) {
        return new ScreenshotLocator(By.id(id));
    }

    public static ScreenshotLocator linkText(String linkText) {
        return new ScreenshotLocator(By.linkText(linkText));
    }

    public static ScreenshotLocator partialLinkText(String linkText) {
        return new ScreenshotLocator(By.partialLinkText(linkText));
    }

    public static ScreenshotLocator name(String name) {
        return new ScreenshotLocator(By.name(name));
    }

    public static ScreenshotLocator tagName(String name) {
        return new ScreenshotLocator(By.tagName(name));
    }

    public static ScreenshotLocator xpath(String xpathExpression) {
        return new ScreenshotLocator(By.xpath(xpathExpression));
    }

    public static ScreenshotLocator className(String className) {
        return new ScreenshotLocator(By.className(className));
    }

    public static ScreenshotLocator cssSelector(String selector) {
        return new ScreenshotLocator(By.cssSelector(selector));
    }

    public static ScreenshotLocator instanceOf(Selector by, String locator) {
        switch (by) {
            case ID:
                return ScreenshotLocator.id(locator);
            case LINK_TEXT:
                return ScreenshotLocator.linkText(locator);
            case PARTIAL_LINK_TEXT:
                return ScreenshotLocator.partialLinkText(locator);
            case NAME:
                return ScreenshotLocator.name(locator);
            case TAG_NAME:
                return ScreenshotLocator.tagName(locator);
            case XPATH:
                return ScreenshotLocator.xpath(locator);
            case CLASS_NAME:
                return ScreenshotLocator.className(locator);
            case CSS_SELECTOR:
                return ScreenshotLocator.cssSelector(locator);
        }
        throw new AssertionError("Locator name " + by + " is not specified.");
    }

    public static List<ScreenshotLocator> getInsertScreenshotLocators() {
        List<ScreenshotLocator> insertScreenshotLocator = new ArrayList<ScreenshotLocator>();
        try {
            Method method = JavaUtils.findMethodByAnnotation(Test.class);
            InsertElement annotation = method.getAnnotation(InsertElement.class);
            if (annotation != null) {
                insertScreenshotLocator.add(ScreenshotLocator.instanceOf(annotation.by(), annotation.locator()));
            } else {
                InsertElements locatorsAnnotation = method.getAnnotation(InsertElements.class);
                if (locatorsAnnotation != null) {
                    for (InsertElement currentAnnotation : locatorsAnnotation.locators()) {
                        insertScreenshotLocator.add(ScreenshotLocator.instanceOf(currentAnnotation.by(), currentAnnotation.locator()));
                    }
                }
            }
            return !insertScreenshotLocator.isEmpty() ? insertScreenshotLocator : null;
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private ScreenshotLocator(By locator) {
        this.locator = locator;
    }

    public ScreenshotLocator setMargins(int marginTop, int marginRight, int marginBottom, int marginLeft) {
        this.marginTop = marginTop;
        this.marginRight = marginRight;
        this.marginBottom = marginBottom;
        this.marginLeft = marginLeft;
        return this;
    }

}
