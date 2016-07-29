package com.wiley.autotest.selenium.context;

/**
 * Created by IntelliJ IDEA.
 * User: maxim
 * Date: 07.02.12
 * Time: 18:56
 */
public interface IComponent extends IPageElement {
    ComponentProvider getComponentProvider();

    void setComponentProvider(ComponentProvider componentProvider);
}
