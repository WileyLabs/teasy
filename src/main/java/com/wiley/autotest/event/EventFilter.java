package com.wiley.autotest.event;

/**
 * Creation date: 31.05.2016
 *
 * @author <a href='mailto:mmukhoyan@wiley.com'>Maxim Mukhoyan</a>
 * @version 1.0
 */

public interface EventFilter {
    <MsgType> void fire(Subscriber<MsgType> each, MsgType message);
}
