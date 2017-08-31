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
        return wrapBase(new TeasyElementData(webElement, by));
    }

    public static <T extends CustomWebElement> T wrap(TeasyElement searchContext, WebElement webElement, By by) {
        return wrapBase(new TeasyElementData(searchContext, webElement, by));
    }

    public static <T extends CustomWebElement> T wrap(WebElement webElement, By by, int index) {
        TeasyElementData teasyElementData = new TeasyElementData(webElement, by);
        teasyElementData.setIndex(index);
        return wrapBase(teasyElementData);
    }

    public static <T extends CustomWebElement> T wrap(TeasyElement searchContext, WebElement webElement, By by, int index) {
        return wrapBase(new TeasyElementData(searchContext, webElement, by, index));
    }

    public static <T extends CustomWebElement> T wrapParent(WebElement webElement) {
        TeasyElementData teasyElementData = new TeasyElementData(webElement);
        return wrapBase(teasyElementData);
    }

    public static <T extends CustomWebElement> List<T> wrapList(List<WebElement> webElementList, By by) {
        List<T> list = new ArrayList<>(webElementList.size());
        for (int i = 0; i < webElementList.size(); i++) {
            list.add(wrap(webElementList.get(i), by, i));
        }
        return list;
    }

    public static <T extends CustomWebElement> List<T> wrapList(TeasyElement searchContext, List<WebElement> webElementList, By by) {
        List<T> list = new ArrayList<>(webElementList.size());
        for (int i = 0; i < webElementList.size(); i++) {
            list.add(wrap(searchContext, webElementList.get(i), by, i));
        }
        return list;
    }

    private static <T extends CustomWebElement> T wrapBase(TeasyElementData teasyElementData) {
        try {
            Class<T> classOfOurWebElement = (Class<T>) Class.forName(SeleniumHolder.getOurWebElementClass());
            return classOfOurWebElement.getDeclaredConstructor(TeasyElementData.class).newInstance(teasyElementData);
        } catch (InstantiationException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            LOGGER.error("Cannot create instance of TeasyElement." + e.getClass().getName() + " occurred. ", e);
            throw new WrapElementException("Cannot create instance of TeasyElement. " + e.getClass().getName() + " occurred. ", e);
        }
    }
}
