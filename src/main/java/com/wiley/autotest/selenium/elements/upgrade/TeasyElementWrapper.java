package com.wiley.autotest.selenium.elements.upgrade;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrap element with TeasyElement
 */
public class TeasyElementWrapper {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(TeasyElementWrapper.class);

    public static <T extends TeasyElement> T wrap(WebElement webElement, By by, TeasyElementType type) {
        return wrapBase(new TeasyElementData(webElement, by), type);
    }

    @Deprecated
    /**
     * will be deleted. just a temp workaround to keep old version supported
     */
    public static <T extends TeasyElement> T wrap_to_be_deleted(WebElement webElement, By by) {
        return wrapBase(new TeasyElementData(webElement, by), TeasyElementType.DOM);
    }

    public static <T extends TeasyElement> T wrap(TeasyElement searchContext, WebElement webElement, By by, TeasyElementType type) {
        return wrapBase(new TeasyElementData(searchContext, webElement, by), type);
    }

    public static <T extends TeasyElement> T wrap(WebElement webElement, By by, int index, TeasyElementType type) {
        return wrapBase(new TeasyElementData(webElement, by, index), type);
    }

    public static <T extends TeasyElement> T wrap(TeasyElement searchContext, WebElement webElement, By by, int index, TeasyElementType type) {
        return wrapBase(new TeasyElementData(searchContext, webElement, by, index), type);
    }

    public static <T extends TeasyElement> List<T> wrapList(List<WebElement> webElementList, By by, TeasyElementType type) {
        List<T> list = new ArrayList<>(webElementList.size());
        for (int i = 0; i < webElementList.size(); i++) {
            list.add(wrap(webElementList.get(i), by, i, type));
        }
        return list;
    }

    public static <T extends TeasyElement> List<T> wrapList(TeasyElement searchContext, List<WebElement> webElementList, By by, TeasyElementType type) {
        List<T> list = new ArrayList<>(webElementList.size());
        for (int i = 0; i < webElementList.size(); i++) {
            list.add(wrap(searchContext, webElementList.get(i), by, i, type));
        }
        return list;
    }

    private static <T extends TeasyElement> T wrapBase(TeasyElementData data, TeasyElementType type) {
        try {

            //TODO !!!IMPORTANT!!! Current approach does not support overloading of element in particular projct
            //TODO VE, NT, consider providing necesarry changes to support it
//            Class<T> classOfOurWebElement = (Class<T>) Class.forName(SeleniumHolder.getOurWebElementClass());

            Class<T> classOfOurWebElement = (Class<T>) Class.forName(type.className());

            return classOfOurWebElement.getDeclaredConstructor(TeasyElementData.class).newInstance(data);
        } catch (InstantiationException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            LOGGER.error("Cannot create instance of TeasyElement." + e.getClass().getName() + " occurred. ", e);
            throw new WrapElementException("Cannot create instance of TeasyElement. " + e.getClass().getName() + " occurred. ", e);
        }
    }
}
