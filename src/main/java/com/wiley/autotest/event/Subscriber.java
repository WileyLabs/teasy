package com.wiley.autotest.event;

public interface Subscriber<MsgType> {
    void notify(MsgType message);
}
