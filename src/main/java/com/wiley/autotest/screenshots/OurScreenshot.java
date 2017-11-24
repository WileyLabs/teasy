package com.wiley.autotest.screenshots;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.coordinates.Coords;
import ru.yandex.qatools.ashot.cropper.indent.IndentCropper;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import java.util.ArrayList;
import java.util.List;

import static com.wiley.autotest.selenium.SeleniumHolder.getWebDriver;
import static com.wiley.autotest.utils.ExecutionUtils.isChrome;
import static ru.yandex.qatools.ashot.cropper.indent.IndentFilerFactory.monochrome;

/**
 * @author <a href="mosadchiy@wiley.com">Mikhail Osadchiy</a>
 */
@Deprecated
/**
 * This class will be deleted in jan 2018. Please use {@link }
 *
 * Provides an opportunity to capture screenshot by the following options:
 * 1) Exclude and include locators
 * 2) Exclude locators
 * 3) Include locators
 * 4) Full page
 */
public class OurScreenshot {

    @Deprecated
    /**
     * use {@link TeasyScreenshot#excludeAndInclude(List, List)}
     */
    public Screenshot excludeAndInclude(List<By> excludeLocators, List<By> includeLocators) {
        WebDriver driver = getWebDriver();
        AShot aShot = new AShot();
        if (isChrome()) {
            hideScrollbar(driver);
            makeViewPortShootingStrategy(aShot);
        }
        addIgnoredAreas(excludeLocators, driver, aShot);
        addMonochromeIndentFilter(aShot);
        Screenshot screenshot = aShot.takeScreenshot(driver, getIncludeElements(includeLocators, driver));
        if (isChrome()) {
            revealScrollbar(driver);
        }
        return screenshot;
    }

    @Deprecated
    /**
     * use {@link TeasyScreenshot#exclude(List)}
     */
    public Screenshot exclude(List<By> excludeLocators) {
        WebDriver driver = getWebDriver();
        AShot aShot = new AShot();
        if (isChrome()) {
            hideScrollbar(driver);
            makeViewPortShootingStrategy(aShot);
        }
        addIgnoredAreas(excludeLocators, driver, aShot);
        Screenshot screenshot = aShot.takeScreenshot(driver);
        if (isChrome()) {
            revealScrollbar(driver);
        }
        return screenshot;
    }

    @Deprecated
    /**
     * use {@link TeasyScreenshot#include(List)}
     */
    public Screenshot include(List<By> includeLocators) {
        WebDriver driver = getWebDriver();
        AShot aShot = new AShot();
        if (isChrome()) {
            hideScrollbar(driver);
            makeViewPortShootingStrategy(aShot);
        }
        addMonochromeIndentFilter(aShot);
        Screenshot screenshot = aShot.takeScreenshot(driver, getIncludeElements(includeLocators, driver));
        if (isChrome()) {
            revealScrollbar(driver);
        }
        return screenshot;
    }

    @Deprecated
    /**
     * use {@link TeasyScreenshot#fullPage()}
     */
    public Screenshot fullPage() {
        WebDriver driver = getWebDriver();
        AShot aShot = new AShot();
        if (isChrome()) {
            hideScrollbar(driver);
            makeViewPortShootingStrategy(aShot);
        }
        Screenshot screenshot = aShot.takeScreenshot(driver);
        if (isChrome()) {
            revealScrollbar(driver);
        }
        return screenshot;
    }

    private void addMonochromeIndentFilter(AShot aShot) {
        aShot.imageCropper(new IndentCropper()
                .addIndentFilter(monochrome()));
    }

    private List<WebElement> getIncludeElements(List<By> includeLocators, WebDriver driver) {
        List<WebElement> includeElements = new ArrayList<>();
        for (By by : includeLocators) {
            includeElements.addAll(driver.findElements(by));
        }
        return includeElements;
    }

    private void addIgnoredAreas(List<By> excludeLocators, WebDriver driver, AShot aShot) {
        for (By excludeLocator : excludeLocators) {
            for (WebElement excludeElement : driver.findElements(excludeLocator)) {
                aShot.addIgnoredArea(new Coords(excludeElement.getLocation().getX(), excludeElement.getLocation().getY(),
                        excludeElement.getSize().getWidth(), excludeElement.getSize().getHeight()));
            }
        }
    }

    private void hideScrollbar(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("document.body.style.overflow = 'hidden';");

    }

    private void makeViewPortShootingStrategy(AShot aShot) {
        aShot.shootingStrategy(ShootingStrategies.viewportPasting(100));
    }

    private void revealScrollbar(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("document.body.style.overflow = 'visible';");
    }
}
