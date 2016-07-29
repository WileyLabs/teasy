package com.wiley.autotest.selenium.context;

import com.google.common.base.Function;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.Reporter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.google.common.collect.Collections2.transform;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.String.format;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang.StringUtils.join;
import static org.openqa.selenium.By.xpath;

/**
 * @author alexey.a.semenov@gmail.com
 */
public class WordsBasedPageLoadingValidator implements PageLoadingValidator {
    private final Log log = LogFactory.getLog(getClass());

    private final Collection<String> words = new ArrayList<String>();

    private By evilWordsLocator;

    private ThreadLocal<Boolean> validationOn = new ThreadLocal<Boolean>();

    @Override
    public void assertLoaded(final WebDriver driver) {
        if (!isValidationOn()) {
            return;
        }

        final List<WebElement> foundEvilWords = driver.findElements(evilWordsLocator);
        if (isNotEmpty(foundEvilWords)) {
            log.fatal("Unexpected evil word found. Pages source:\n" + driver.getPageSource());
            lookForConcreteEvilWord(driver);
            fail("oops! I found evil word, but didn't find element with it");
        }
    }

    private void lookForConcreteEvilWord(final WebDriver driver) {
        for (final String each : words) {
            final List<WebElement> found = driver.findElements(xpath(format("//*[contains(text(), '%s')]", each)));
            if (isNotEmpty(found)) {
                fail(format("Evil word found: '%s' in tag '%s'", each, found.get(0).getTagName()));
            }
        }
    }

    public void setWords(final Collection<String> words) {
        this.words.clear();
        this.words.addAll(words);
        evilWordsLocator = xpath(format("//*[(%s) and not(name() = 'SCRIPT') and not(name() = 'script')]", join(selectorsFor(words), " or ")));
        validationOn.set(TRUE);
    }

    public Boolean isValidationOn() {
        return validationOn.get() != null ? validationOn.get() : FALSE;
    }

    public void setValidationOn() {
        validationOn.set(TRUE);
    }

    public void setValidationOff() {
        validationOn.set(FALSE);
    }

    private Collection<String> selectorsFor(final Collection<String> words) {
        return transform(words, new Function<String, String>() {
            @Override
            public String apply(final String input) {
                return format("contains(text(), '%s')", input);
            }
        });
    }

    private void fail(final String message) {
        Reporter.log(message);
        Assert.fail(message);
    }
}
