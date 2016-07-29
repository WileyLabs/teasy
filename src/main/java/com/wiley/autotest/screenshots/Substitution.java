package com.wiley.autotest.screenshots;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mosadchiy on 26.05.2016.
 */

public class Substitution<P> {
    private P target;
    private String text;
    private static SAXBuilder saxBuilder = new SAXBuilder();
    protected final Log logger = LogFactory.getLog(this.getClass());

    public Substitution(P target, String text) {
        this.target = target;
        this.text = text;
    }

    public List<WebElement> getWebElements(WebDriver webDriver) {
        return target instanceof WebElement ? Arrays.asList((WebElement) target) : webDriver.findElements((By) target);
    }

    public String getText() {
        return text;
    }

    /*
     * The method returns null by default or you can override it and parse dom tree using XMLOutputter instead of simple string
     */
    protected Element getJDOM(Element element, String text) {
        return null;
    }

    protected String getHTML(Element element, String text) {
        Element jdom = getJDOM(element, text);
        if (jdom != null) {
            XMLOutputter outputter = new XMLOutputter();
            return outputter.outputString(jdom.getContent());
        }
        return text;
    }

    final String getHTML(WebElement webElement) {
        Element element;
        try {
            String outerHTML = webElement.getAttribute("outerHTML");
            Document doc = saxBuilder.build(new StringReader(outerHTML));
            element = doc.getRootElement();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return text;
        }
        return getHTML(element, text);
    }

    public static void substitute(final WebDriver webDriver, WebElement element, String substitute) {
        String js = "arguments[0].innerHTML='" + StringEscapeUtils.escapeJavaScript(substitute) + "'";
        ((JavascriptExecutor) webDriver).executeScript(js, element);
    }
}