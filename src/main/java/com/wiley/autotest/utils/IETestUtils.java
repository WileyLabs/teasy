package com.wiley.autotest.utils;

import org.openqa.selenium.WebElement;

/**
 * User: vefimov
 * Date: 05.09.2014
 * Time: 15:48
 */
public class IETestUtils {
    public static boolean isActionElements(WebElement element) {
        return "input".equals(element.getTagName()) && "checkbox".equals(element.getAttribute("type"));
    }

    public static boolean isNotOptionInput(WebElement element) {
        return !"option".equals(element.getTagName()) && !"input".equals(element.getTagName()) && !"img".equals(element.getTagName()) && !"div".equals(element.getTagName());
    }

    public static boolean isNotLink(WebElement element) {
        return !"a".equals(element.getTagName()) && !"auto".equals(element.getCssValue("height"));
    }

    public static boolean isLink(WebElement element) {
        return "a".equals(element.getTagName());
    }

    public static boolean isNotTd(WebElement element) {
        return !"td".equals(element.getTagName());
    }

    public static boolean isTdTag(WebElement element) {
        return "td".equals(element.getTagName());
    }

    public static boolean isDivTag(WebElement element) {
        return "div".equals(element.getTagName());
    }

    public static boolean isLiTag(WebElement element) {
        return "li".equals(element.getTagName());
    }

    public static boolean isNotCheckBoxLiSpan(WebElement element) {
        return !"checkbox".equals(element.getAttribute("type")) && !"li".equals(element.getTagName()) && !"span".equals(element.getTagName()) && !"p".equals(element.getTagName());
    }

    public static boolean isNotTable(WebElement element) {
        return !"table".equals(element.getTagName());
    }

    public static boolean isInputOrSelectOrCheckboxElement(WebElement element){
        return "input".equals(element.getTagName()) || "select".equals(element.getTagName()) || "option".equals(element.getTagName());
    }

    public static boolean isDisabledElement(WebElement element){
        return element.getAttribute("disabled") != null && element.getAttribute("disabled").equals("true");
    }
}
