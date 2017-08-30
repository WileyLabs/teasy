package com.wiley.autotest.selenium.elements.upgrade;

import com.wiley.autotest.selenium.SeleniumHolder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ntyukavkin
 * Date: 17.11.2016
 * Time: 16:57
 */
public class CustomWebElementFactory {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CustomWebElementFactory.class);

    public static <T extends CustomWebElement> T wrap(WebElement webElement, By by) {
        return wrapBase(new TeasyWebElementData(webElement, by));
    }

    public static <T extends CustomWebElement> T wrap(TeasyWebElement searchContext, WebElement webElement, By by) {
        return wrapBase(new TeasyWebElementData(searchContext, webElement, by));
    }

    public static <T extends CustomWebElement> T wrap(WebElement webElement, By by, int index) {
        TeasyWebElementData teasyWebElementData = new TeasyWebElementData(webElement, by);
        teasyWebElementData.setIndex(index);
        return wrapBase(teasyWebElementData);
    }

    public static <T extends CustomWebElement> T wrap(TeasyWebElement searchContext, WebElement webElement, By by, int index) {
        return wrapBase(new TeasyWebElementData(searchContext, webElement, by, index));
    }

    public static <T extends CustomWebElement> T wrapParent(WebElement webElement) {
        TeasyWebElementData teasyWebElementData = new TeasyWebElementData(webElement);
        return wrapBase(teasyWebElementData);
    }

    public static <T extends CustomWebElement> List<T> wrapList(List<WebElement> webElementList, By by) {
        List<T> list = new ArrayList<>(webElementList.size());
        for (int i = 0; i < webElementList.size(); i++) {
            list.add(wrap(webElementList.get(i), by, i));
        }
        return list;
    }

    public static <T extends CustomWebElement> List<T> wrapList(TeasyWebElement searchContext, List<WebElement> webElementList, By by) {
        List<T> list = new ArrayList<>(webElementList.size());
        for (int i = 0; i < webElementList.size(); i++) {
            list.add(wrap(searchContext, webElementList.get(i), by, i));
        }
        return list;
    }

    private static <T extends CustomWebElement> T wrapBase(TeasyWebElementData teasyWebElementData) {
        try {
            Class<T> classOfOurWebElement = (Class<T>) Class.forName(SeleniumHolder.getOurWebElementClass());
            return classOfOurWebElement.getDeclaredConstructor(TeasyWebElementData.class).newInstance(teasyWebElementData);
        } catch (InstantiationException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            LOGGER.error("Cannot create instance of TeasyWebElement." + e.getClass().getName() + " occurred. ", e);
            throw new WrapElementException("Cannot create instance of TeasyWebElement. " + e.getClass().getName() + " occurred. ", e);
        }
    }
}
