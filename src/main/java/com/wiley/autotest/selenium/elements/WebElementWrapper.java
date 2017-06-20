package com.wiley.autotest.selenium.elements;

import com.wiley.autotest.selenium.elements.upgrade.OurWebElement;
import com.wiley.autotest.selenium.extensions.ElementFactory;
import com.wiley.autotest.selenium.extensions.internal.DefaultElementFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * User: maxim
 * Date: 14.02.12
 */
public class WebElementWrapper {


    //TODO VE refactor this to keep only OurWebElement!

    public final ElementFactory elementFactory = new DefaultElementFactory();
    public WebElement webElement;
    public OurWebElement element;

    public WebElementWrapper(final WebElement webElement) {
        this.webElement = webElement;
    }

    public WebElementWrapper(final OurWebElement element) {
        this.element = element;
    }

    public Select getSelect() {
        if (element != null) {
            return elementFactory.create(Select.class, element);
        } else {
            return elementFactory.create(Select.class, webElement);
        }
    }

    public Button getButton() {
        if (element != null) {
            return elementFactory.create(Button.class, element);
        } else {
            return elementFactory.create(Button.class, webElement);
        }
    }

    public Link getLink() {
        if (element != null) {
            return elementFactory.create(Link.class, element);
        } else {
            return elementFactory.create(Link.class, webElement);
        }
    }

    public TextField getTextField() {
        if (element != null) {
            return elementFactory.create(TextField.class, element);
        } else {
            return elementFactory.create(TextField.class, webElement);
        }
    }

    public RadioButton getRadioButton() {
        if (element != null) {
            return elementFactory.create(RadioButton.class, element);
        } else {
            return elementFactory.create(RadioButton.class, webElement);
        }
    }

    public CheckBox getCheckBox() {
        if (element != null) {
            return elementFactory.create(CheckBox.class, element);
        } else {
            return elementFactory.create(CheckBox.class, webElement);
        }
    }

    public <T extends Element> T getElement(Class<T> elementType, By by) {
        return elementFactory.create(elementType, element, by);
    }
}
