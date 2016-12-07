package com.wiley.autotest.event;

@FunctionalInterface
public interface Subscriber<MsgType> {

    void notify(MsgType message);
}
