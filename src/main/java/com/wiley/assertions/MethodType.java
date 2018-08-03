package com.wiley.assertions;

public enum MethodType {

    TEST {
        @Override
        public String desc() {
            return "Test method";
        }
    },

    CONFIG {
        @Override
        public String desc() {
            return "Configuration method";
        }
    };

    public abstract String desc();
}
