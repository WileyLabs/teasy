package com.wiley.autotest.selenium.elements.upgrade;

import com.wiley.autotest.selenium.SeleniumHolder;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
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
public class OurWebElementFactory {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(OurWebElementFactory.class);

    public static <T extends IOurWebElement> T wrap(WebElement webElement, By by) {
        return wrapBase(new OurWebElementData(webElement, by));
    }

    public static <T extends IOurWebElement> T wrap(OurWebElement searchContext, WebElement webElement, By by) {
        return wrapBase(new OurWebElementData(searchContext, webElement, by));
    }

    public static <T extends IOurWebElement> T wrap(WebElement webElement, By by, int index) {
        OurWebElementData ourWebElementData = new OurWebElementData(webElement, by);
        ourWebElementData.setIndex(index);
        return wrapBase(ourWebElementData);
    }

    public static <T extends IOurWebElement> T wrap(OurWebElement searchContext, WebElement webElement, By by, int index) {
        return wrapBase(new OurWebElementData(searchContext, webElement, by, index));
    }

    public static <T extends IOurWebElement> T wrapParent(WebElement webElement) {
        OurWebElementData ourWebElementData = new OurWebElementData(webElement);
        return wrapBase(ourWebElementData);
    }

    public static <T extends IOurWebElement> List<T> wrapList(List<WebElement> webElementList, By by) {
        List<T> list = new ArrayList<>(webElementList.size());
        for (int i = 0; i < webElementList.size(); i++) {
            list.add(wrap(webElementList.get(i), by, i));
        }
        return list;
    }

    public static <T extends IOurWebElement> List<T> wrapList(OurWebElement searchContext, List<WebElement> webElementList, By by) {
        List<T> list = new ArrayList<>(webElementList.size());
        for (int i = 0; i < webElementList.size(); i++) {
            list.add(wrap(searchContext, webElementList.get(i), by, i));
        }
        return list;
    }

    private static <T extends IOurWebElement> T wrapBase(OurWebElementData ourWebElementData) {
        try {
            Class<T> classOfOurWebElement = (Class<T>) Class.forName(SeleniumHolder.getOurWebElementClass());
            return classOfOurWebElement.getDeclaredConstructor(OurWebElementData.class).newInstance(ourWebElementData);
        } catch (InstantiationException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            LOGGER.error("Cannot create instance of OurWebElement." + e.getClass().getName() + " occurred. ", e);
            throw new WrapElementException("Cannot create instance of OurWebElement. " + e.getClass().getName() + " occurred. ", e);
        }
    }
}
