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
public class OurWebElementFactory {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(OurWebElementFactory.class);

    public static <T extends IOurWebElement> T wrap(WebElement webElement, By by) {
        OurWebElementData ourWebElementData = new OurWebElementData();
        ourWebElementData.setElement(webElement);
        ourWebElementData.setBy(by);
        return wrapBase(ourWebElementData);
    }

    public static <T extends IOurWebElement> T wrap(WebElement webElement, By by, int index) {
        OurWebElementData ourWebElementData = new OurWebElementData();
        ourWebElementData.setElement(webElement);
        ourWebElementData.setBy(by);
        ourWebElementData.setIndex(index);
        return wrapBase(ourWebElementData);
    }

    public static <T extends IOurWebElement> T wrapParent(WebElement webElement) {
        OurWebElementData ourWebElementData = new OurWebElementData();
        ourWebElementData.setElement(webElement);
        return wrapBase(ourWebElementData);
    }

    public static List<WebElement> wrapList(List<WebElement> webElementList, By by) {
        List<WebElement> list = new ArrayList<>(webElementList.size());
        for (int i = 0; i < webElementList.size(); i++) {
            list.add(wrap(webElementList.get(i), by, i));
        }
        return list;
    }

    private static <T extends IOurWebElement> T wrapBase(OurWebElementData ourWebElementData) {
        try {
            Class<T> classOfOurWebElement = getOurWebElementClass();
            return classOfOurWebElement.getDeclaredConstructor(OurWebElementData.class).newInstance(ourWebElementData);
        } catch (InstantiationException e) {
            LOGGER.error("Cannot create instance of OurWebElement. InstantiationException occurs", e);
            throw new WrapElementException("Cannot create instance of OurWebElement. InstantiationException occurs", e);
        } catch (IllegalAccessException e) {
            LOGGER.error("Cannot create instance of OurWebElement. IllegalAccessException occurs", e);
            throw new WrapElementException("Cannot create instance of OurWebElement. IllegalAccessException occurs", e);
        } catch (InvocationTargetException e) {
            LOGGER.error("Cannot create instance of OurWebElement. InvocationTargetException occurs", e);
            throw new WrapElementException("Cannot create instance of OurWebElement. InvocationTargetException occurs", e);
        } catch (NoSuchMethodException e) {
            LOGGER.error("Cannot create instance of OurWebElement. NoSuchMethodException occurs", e);
            throw new WrapElementException("Cannot create instance of OurWebElement. NoSuchMethodException occurs", e);
        } catch (ClassNotFoundException e) {
            LOGGER.error("Cannot create instance of OurWebElement. ClassNotFoundException occurs", e);
            throw new WrapElementException("Cannot create instance of OurWebElement. ClassNotFoundException occurs", e);
        }
    }

    private static <T extends IOurWebElement> Class<T> getOurWebElementClass() throws ClassNotFoundException {
        return (Class<T>) Class.forName(SeleniumHolder.getOurWebElementClass());
    }
}
