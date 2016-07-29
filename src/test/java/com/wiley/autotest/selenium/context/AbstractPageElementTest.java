package com.wiley.autotest.selenium.context;

import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;

/**
 * User: dfedorov
 * Date: 4/23/12
 * Time: 10:33 AM
 */
public class AbstractPageElementTest {
    private AbstractMockedPageElement abstractPageElement;

    @BeforeMethod
    public void setUp() {
        abstractPageElement = getMockedAbstractPageElement();
    }

    @Test(enabled = false)
    public void testChooseRandomly() throws Exception {
        final int sourceListSize = 10;
        final int itemsToSelect = 3;
        final List<WebElement> sourceList = new ArrayList<WebElement>(sourceListSize);
        for (int i = 0; i < sourceListSize; i++) {
            sourceList.add(mock(WebElement.class));
        }

        final List<WebElement> resultList = abstractPageElement.runChooseRandomly(sourceList, itemsToSelect);
        assertThat(resultList.size(), equalTo(itemsToSelect));
        assertThat(resultList.get(0), not(equalTo(resultList.get(1))));
        assertThat(resultList.get(0), not(equalTo(resultList.get(2))));
        assertThat(resultList.get(1), not(equalTo(resultList.get(2))));
    }

    public AbstractMockedPageElement getMockedAbstractPageElement() {
        return new AbstractMockedPageElement() {
            public <T> List<T> runChooseRandomly(final List<T> sourceList, final int itemsToSelect) {
                return getRandomElementsInList(sourceList, itemsToSelect);
            }
        };
    }

    private abstract class AbstractMockedPageElement extends AbstractPageElement {
        public abstract <T> List<T> runChooseRandomly(final List<T> sourceList, final int itemsToSelect);
    }
}
