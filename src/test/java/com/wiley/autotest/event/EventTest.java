package com.wiley.autotest.event;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.apache.commons.lang.math.RandomUtils.nextInt;
import static org.mockito.Mockito.*;

public class EventTest {
    private TestEvent event;

    @BeforeMethod
    public void setUp() {
        event = new TestEvent();
    }

    @Test(enabled = false)
    public void testNotifySubscriber() {
        final TestSubscriber subscriber = givenSubscriber();
        final int message = nextInt();

        event.fire(message);

        verify(subscriber).notify(message);
    }

    @Test(enabled = false)
    public void testNotifyMultipleSubscribers() {
        final int message = nextInt();
        final TestSubscriber first = givenSubscriber();
        final TestSubscriber second = givenSubscriber();

        event.fire(message);

        verify(first).notify(message);
        verify(second).notify(message);
    }

    @Test(enabled = false)
    public void testUnsubscribedSubscriberIsNotNotified() {
        final TestSubscriber subscriber = givenSubscriber();

        event.unsubscribeAll();
        event.fire(nextInt());

        verify(subscriber, never()).notify(anyInt());
    }

    private TestSubscriber givenSubscriber() {
        final TestSubscriber subscriber = mock(TestSubscriber.class);
        event.subscribe(subscriber);
        return subscriber;
    }
}

class TestEvent extends Event<Integer> {

}

class TestSubscriber implements Subscriber<Integer> {
    @Override
    public void notify(final Integer message) {
    }
}
