package com.wiley.utils;

import com.wiley.holders.DriverHolder;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

/**
 * Javascript actions through WebDriver
 * A wrapper around WebDriver taken from a SeleniumHolder
 */
public final class JsActions {

    private JsActions() {
    }

    public static void dragAndDrop(WebElement dragElement, WebElement dropTo) {
        executeScript(
                "function dnd(elemDrag, elemDrop) {\n" +
                        "    var DELAY_INTERVAL_MS = 100;\n" +
                        "    var MAX_TRIES = 10;\n" +
                        "    var dragStartEvent;\n" +
                        "    if (!elemDrag || !elemDrop) {\n" +
                        "        return false;\n" +
                        "    }\n" +
                        "    function fireMouseEvent(type, elem, dataTransfer) {\n" +
                        "        var evt = document.createEvent('MouseEvents');\n" +
                        "        evt.initMouseEvent(type, true, true, window, 1, 1, 1, 0, 0, false, false, false, false, 0, elem);\n" +
                        "        if (/^dr/i.test(type)) {\n" +
                        "            evt.dataTransfer = dataTransfer || createNewDataTransfer();\n" +
                        "        }\n" +
                        "        elem.dispatchEvent(evt);\n" +
                        "        return evt;\n" +
                        "    }\n" +
                        "    function createNewDataTransfer() {\n" +
                        "        var data = {};\n" +
                        "        return {\n" +
                        "            clearData: function (key) {\n" +
                        "                if (key === undefined) {\n" +
                        "                    data = {};\n" +
                        "                } else {\n" +
                        "                    delete data[key];\n" +
                        "                }\n" +
                        "            },\n" +
                        "            getData: function (key) {\n" +
                        "                return data[key];\n" +
                        "            },\n" +
                        "            setData: function (key, value) {\n" +
                        "                data[key] = value;\n" +
                        "            },\n" +
                        "            setDragImage: function () {\n" +
                        "            },\n" +
                        "            dropEffect: 'none',\n" +
                        "            files: [],\n" +
                        "            items: [],\n" +
                        "            types: []\n" +
                        "        }\n" +
                        "    }\n" +
                        "    fireMouseEvent('mousedown', elemDrag);\n" +
                        "    dragStartEvent = fireMouseEvent('dragstart', elemDrag);\n" +
                        "    function dragover() {\n" +
                        "        fireMouseEvent('dragover', elemDrop, dragStartEvent.dataTransfer);\n" +
                        "    }\n" +
                        "    function drop() {\n" +
                        "        fireMouseEvent('drop', elemDrop, dragStartEvent.dataTransfer);\n" +
                        "        fireMouseEvent('mouseup', elemDrop);\n" +
                        "        fireMouseEvent('dragend', elemDrag);\n" +
                        "    }\n" +
                        "    setTimeout(dragover, DELAY_INTERVAL_MS);\n" +
                        "    setTimeout(drop, DELAY_INTERVAL_MS * 2);\n" +
                        "    return true;\n" +
                        "}\n" +
                        "   dnd(arguments[0], arguments[1])",
                dragElement, dropTo);
    }

    /**
     * {@link JavascriptExecutor#executeScript(String, Object...)}
     */
    public static Object executeScript(String script, Object... args) {
        return driver().executeScript(script, args);
    }

    /**
     * {@link JavascriptExecutor#executeAsyncScript(String, Object...)}
     */
    public static Object executeAsyncScript(String script, Object... args) {
        return driver().executeAsyncScript(script, args);
    }

    private static JavascriptExecutor driver() {
        return (JavascriptExecutor) DriverHolder.getDriver();
    }
}
