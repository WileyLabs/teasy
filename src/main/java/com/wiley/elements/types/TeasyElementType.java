package com.wiley.elements.types;

/**
 * Representation of different Teasy Elements
 * VISIBLE - element that is displayed to the user
 * DOM - element that is present in DOM but not necessarily is visible
 * NULL - represents a not-found-element. It is needed to manage conditions like "absent".
 */
public enum TeasyElementType {

    VISIBLE {
        @Override
        public String className() {
            return "com.wiley.autotest.selenium.elements.upgrade.VisibleTeasyElement";
        }
    },
    DOM {
        @Override
        public String className() {
            return "com.wiley.autotest.selenium.elements.upgrade.DomTeasyElement";
        }
    },
    NULL {
        @Override
        public String className() {
            return "com.wiley.autotest.selenium.elements.upgrade.NullTeasyElement";
        }
    };

    public abstract String className();
}
