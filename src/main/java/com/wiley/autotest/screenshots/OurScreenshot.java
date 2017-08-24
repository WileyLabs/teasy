package com.wiley.autotest.screenshots;

import com.wiley.autotest.utils.ExecutionUtils;
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
import static ru.yandex.qatools.ashot.cropper.indent.IndentFilerFactory.monochrome;

/**
 * Created by mosadchiy on 22.08.2017.
 */
public class OurScreenshot {

    public Screenshot excludeAndInclude(List<By> excludeLocators, List<By> includeLocators) {
        WebDriver driver = getWebDriver();
        boolean isChrome = ExecutionUtils.isChrome();
        Screenshot screenshot;
        AShot aShot = new AShot();
        if (isChrome) {
            ((JavascriptExecutor) driver).executeScript("document.body.style.overflow = 'hidden';");
            aShot.shootingStrategy(ShootingStrategies.viewportPasting(100));
        }
        for (By excludeLocator : excludeLocators) {
            for (WebElement excludeElement : driver.findElements(excludeLocator)) {
                aShot.addIgnoredArea(new Coords(excludeElement.getLocation().getX(), excludeElement.getLocation().getY(),
                        excludeElement.getSize().getWidth(), excludeElement.getSize().getHeight()));
            }
        }
        List<WebElement> includeElements = new ArrayList<>();
        for (By by : includeLocators) {
            includeElements.addAll(driver.findElements(by));
        }
        screenshot = aShot.imageCropper(new IndentCropper()
                .addIndentFilter(monochrome()))
                .takeScreenshot(driver, includeElements);
        if (isChrome) {
            ((JavascriptExecutor) driver).executeScript("document.body.style.overflow = 'visible';");
        }
        return screenshot;
    }

    public Screenshot exclude(List<By> excludeLocators) {
        WebDriver driver = getWebDriver();
        boolean isChrome = ExecutionUtils.isChrome();
        Screenshot screenshot;
        AShot aShot = new AShot();
        if (isChrome) {
            ((JavascriptExecutor) driver).executeScript("document.body.style.overflow = 'hidden';");
            aShot.shootingStrategy(ShootingStrategies.viewportPasting(100));
        }
        for (By excludeLocator : excludeLocators) {
            for (WebElement excludeElement : driver.findElements(excludeLocator)) {
                aShot.addIgnoredArea(new Coords(excludeElement.getLocation().getX(), excludeElement.getLocation().getY(),
                        excludeElement.getSize().getWidth(), excludeElement.getSize().getHeight()));
            }
        }
        screenshot = aShot.takeScreenshot(driver);
        if (isChrome) {
            ((JavascriptExecutor) driver).executeScript("document.body.style.overflow = 'visible';");
        }
        return screenshot;
    }

    public Screenshot include(List<By> includeLocators) {
        WebDriver driver = getWebDriver();
        boolean isChrome = ExecutionUtils.isChrome();
        Screenshot screenshot;
        AShot aShot = new AShot();
        if (isChrome) {
            ((JavascriptExecutor) driver).executeScript("document.body.style.overflow = 'hidden';");
            aShot.shootingStrategy(ShootingStrategies.viewportPasting(100));
        }
        List<WebElement> includeElements = new ArrayList<>();
        for (By by : includeLocators) {
            includeElements.addAll(driver.findElements(by));
        }
        screenshot = aShot.imageCropper(new IndentCropper()
                .addIndentFilter(monochrome()))
                .takeScreenshot(driver, includeElements);
        if (isChrome) {
            ((JavascriptExecutor) driver).executeScript("document.body.style.overflow = 'visible';");
        }
        return screenshot;
    }

    public Screenshot fullPage() {
        WebDriver driver = getWebDriver();
        boolean isChrome = ExecutionUtils.isChrome();
        Screenshot screenshot;
        AShot aShot = new AShot();
        if (isChrome) {
            ((JavascriptExecutor) driver).executeScript("document.body.style.overflow = 'hidden';");
            aShot.shootingStrategy(ShootingStrategies.viewportPasting(100));
        }
        screenshot = aShot.takeScreenshot(driver);
        if (isChrome) {
            ((JavascriptExecutor) driver).executeScript("document.body.style.overflow = 'visible';");
        }
        return screenshot;
    }
}
