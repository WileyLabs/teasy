package com.wiley.autotest.matchers;

import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.wiley.autotest.matchers.SortingMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: dfedorov
 * Date: 3/21/12
 * Time: 9:12 AM
 */
public class SortingMatchersTest {

    @Test(enabled = false)
    public void testSortedInAscendingOrder() throws Exception {
        final List<WebElement> elements = new ArrayList<WebElement>();
        assertThat(elements, sortedInAscendingOrder());

        elements.add(getMockedWebElementWithText("aaa"));
        assertThat(elements, sortedInAscendingOrder());

        elements.add(getMockedWebElementWithText("BBB"));
        assertThat(elements, sortedInAscendingOrder());

        elements.add(getMockedWebElementWithText("BBB"));
        assertThat(elements, sortedInAscendingOrder());

        elements.add(getMockedWebElementWithText("aaa"));
        assertThat(elements, not(sortedInAscendingOrder()));
    }

    @Test(enabled = false)
    public void testSortedInDescendingOrder() throws Exception {
        final List<WebElement> elements = new ArrayList<WebElement>();
        assertThat(elements, sortedInDescendingOrder());

        elements.add(getMockedWebElementWithText("BBB"));
        assertThat(elements, sortedInDescendingOrder());

        elements.add(getMockedWebElementWithText("aaa"));
        assertThat(elements, sortedInDescendingOrder());

        elements.add(getMockedWebElementWithText("aaa"));
        assertThat(elements, sortedInDescendingOrder());

        elements.add(getMockedWebElementWithText("BBB"));
        assertThat(elements, not(sortedInDescendingOrder()));
    }

    @Test(enabled = false)
    public void testSortingTextWithDigits1() {
        final List<WebElement> elements = new ArrayList<WebElement>();
        elements.add(getMockedWebElementWithText("lo 10.1.0 loTitle"));
        elements.add(getMockedWebElementWithText("lo 10.2.0 loTitle"));
        elements.add(getMockedWebElementWithText("lo 10.11.0 loTitle"));
        assertThat(elements, sortedInAscendingOrder());
    }

    @Test(enabled = false)
    public void testSortingTextWithDigits2() {
        final List<WebElement> elements = new ArrayList<WebElement>();
        elements.add(getMockedWebElementWithText("lo 10.1.0 loTitle"));
        elements.add(getMockedWebElementWithText("lo 10.11.0 loTitle"));
        elements.add(getMockedWebElementWithText("lo 10.2.0 loTitle"));
        assertThat(elements, not(sortedInAscendingOrder()));
    }

    @Test(enabled = false)
    public void testSortedDatesInAscendingOrder() throws Exception {
        final List<WebElement> elements = new ArrayList<WebElement>();
        assertThat(elements, sortedDatesInAscendingOrder());

        elements.add(getMockedWebElementWithText("Mar 01 12:38 PM"));
        assertThat(elements, sortedDatesInAscendingOrder());

        elements.add(getMockedWebElementWithText("Mar 01 12:38 PM"));
        assertThat(elements, sortedDatesInAscendingOrder());

        elements.add(getMockedWebElementWithText("May 01 12:38 PM"));
        assertThat(elements, sortedDatesInAscendingOrder());

        elements.add(getMockedWebElementWithText("Mar 01 12:38 AM"));
        assertThat(elements, not(sortedDatesInAscendingOrder()));
    }

    @Test(enabled = false)
    public void testSortedDatesInDescendingOrder() throws Exception {
        final List<WebElement> elements = new ArrayList<WebElement>();
        assertThat(elements, sortedDatesInDescendingOrder());

        elements.add(getMockedWebElementWithText("Apr 01 12:38 PM"));
        assertThat(elements, sortedDatesInDescendingOrder());

        elements.add(getMockedWebElementWithText("Apr 01 12:38 PM"));
        assertThat(elements, sortedDatesInDescendingOrder());

        elements.add(getMockedWebElementWithText("Mar 01 12:38 PM"));
        assertThat(elements, sortedDatesInDescendingOrder());

        elements.add(getMockedWebElementWithText("Apr 01 12:38 PM"));
        assertThat(elements, not(sortedDatesInDescendingOrder()));
    }

    @Test(enabled = false)
    public void testSortedDatesInMultithreadedEnvironment() throws Exception {
        final int loopCount = 100;
        final ExecutorService pool = Executors.newFixedThreadPool(2);
        final Callable<Integer> thread1 = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                for (int i = 0; i < loopCount; i++) {
                    final List<WebElement> elements = new ArrayList<WebElement>();
                    elements.add(getMockedWebElementWithText("Apr 01 12:38 PM"));
                    assertThat(elements, sortedDatesInAscendingOrder());
                    assertThat(elements, sortedDatesInDescendingOrder());

                    elements.add(getMockedWebElementWithText("Apr 01 12:38 PM"));
                    assertThat(elements, sortedDatesInAscendingOrder());
                    assertThat(elements, sortedDatesInDescendingOrder());

                    elements.add(getMockedWebElementWithText("Mar 01 12:38 PM"));
                    assertThat(elements, not(sortedDatesInAscendingOrder()));
                    assertThat(elements, sortedDatesInDescendingOrder());

                    elements.add(getMockedWebElementWithText("Apr 01 12:38 PM"));
                    assertThat(elements, not(sortedDatesInAscendingOrder()));
                    assertThat(elements, not(sortedDatesInDescendingOrder()));
                }

                return 0;
            }
        };

        final Callable<Integer> thread2 = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                final String pattern = "MMM, dd";

                for (int i = 0; i < loopCount; i++) {
                    final List<WebElement> elements = new ArrayList<WebElement>();
                    elements.add(getMockedWebElementWithText("Apr, 01"));
                    assertThat(elements, sortedDatesInAscendingOrder(pattern));
                    assertThat(elements, sortedDatesInDescendingOrder(pattern));

                    elements.add(getMockedWebElementWithText("Apr, 01"));
                    assertThat(elements, sortedDatesInAscendingOrder(pattern));
                    assertThat(elements, sortedDatesInDescendingOrder(pattern));

                    elements.add(getMockedWebElementWithText("Mar, 01"));
                    assertThat(elements, not(sortedDatesInAscendingOrder(pattern)));
                    assertThat(elements, sortedDatesInDescendingOrder(pattern));

                    elements.add(getMockedWebElementWithText("Apr, 01"));
                    assertThat(elements, not(sortedDatesInAscendingOrder(pattern)));
                    assertThat(elements, not(sortedDatesInDescendingOrder(pattern)));
                }

                return 0;
            }
        };

        Future<Integer> future1 = pool.submit(thread1);
        Future<Integer> future2 = pool.submit(thread2);
        assertThat(future1.get(), is(0));
        assertThat(future2.get(), is(0));
    }

    private WebElement getMockedWebElementWithText(final String text) {
        final WebElement element = mock(WebElement.class);
        when(element.getText()).thenReturn(text);

        return element;
    }

}
