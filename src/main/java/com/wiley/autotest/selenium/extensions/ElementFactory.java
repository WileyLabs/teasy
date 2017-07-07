package com.wiley.autotest.selenium.extensions;

import com.wiley.autotest.selenium.elements.Element;
import com.wiley.autotest.selenium.elements.upgrade.OurWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public interface ElementFactory {

    <T extends Element> T create(Class<T> classOfElement, WebElement wrappedElement);

    <T extends Element> T create(Class<T> classOfElement, WebElement wrappedElement, By by);

    <T extends Element> T create(Class<T> classOfElement, OurWebElement wrappedElement);

    <T extends Element> T create(Class<T> classOfElement, OurWebElement wrappedElement, By by);
}
