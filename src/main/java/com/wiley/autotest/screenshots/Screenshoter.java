package com.wiley.autotest.screenshots;

import com.wiley.autotest.selenium.driver.FramesTransparentWebDriver;
import io.qameta.allure.Attachment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.Alert;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.wiley.autotest.screenshots.ScreenShotsPathsHolder.addScreenShotPathForTest;
import static com.wiley.autotest.selenium.SeleniumHolder.getWebDriver;

public class Screenshoter {

    private static final Log LOGGER = LogFactory.getLog(Screenshoter.class);

    @Attachment(value = "{0} : {1}", type = "image/png")
    public byte[] attachScreenShotToAllure(final String testName, final String errorMessage, final File screenShot) throws IOException {
        return Files.readAllBytes(screenShot.toPath());
    }

    public synchronized void takeScreenshot(final String errorMessage, final String testName) {
        try {
//This probably could be used someday (do not delete)
//            int jsErrorNumber = JavaScriptError.readErrors(getWebDriver()).size();
//            printStrings(image, removeNL(testName, errorMessage, "The following number of JS errors appeared during the test: " + jsErrorNumber));

            //switch to default context to avoid issue when being in a frame screenshot coordinates are wrong
            WebDriver driver = ((FramesTransparentWebDriver) getWebDriver()).getDriver();
            driver.switchTo().defaultContent();

            BufferedImage image = new TeasyScreenshot(getWebDriver()).fullPage().getImage();
            printStrings(image, removeNL(testName, errorMessage));

            final String pathName = getFilenameFor(testName);
            final File screenShotWithProjectPath = new File(pathName);
            ImageIO.write(image, "png", screenShotWithProjectPath);
            addScreenShotPathForTest(getOriginalTestName(testName), screenShotWithProjectPath.getPath(), errorMessage);

            attachScreenShotToAllure(errorMessage, testName, screenShotWithProjectPath);

//VE path below works for TestNG (do not delete)
//            String pathToImage = "../../ws/test-reports/test-classes/" + screenShotWithProjectPath.getName();
//VE path below works for ReportNG
            String pathToImage = "../../test-reports/test-classes/" + screenShotWithProjectPath.getName();

            Reporter.log("<br/><a href='" + pathToImage + "' target='_blank'> <img src='" + pathToImage + "' height='100' width='100'/> </a><br/>");
        } catch (IOException e) {
            LOGGER.error("IOException occurs", e);
        } catch (UnhandledAlertException alertException) {
            Alert alert = getWebDriver().switchTo().alert();
            String alertText = alert.getText();
            LOGGER.error("*****ERROR***** Unexpected Alert appeared. Alert text " + alertText);
            alert.dismiss();
            takeScreenshot(errorMessage, testName);
        }
    }

    private Collection<String> removeNL(final String... strings) {
        final Collection<String> result = new ArrayList<String>();
        for (final String each : strings) {
            if (each != null) {
                result.addAll(Arrays.asList(each.split("\n")));
            }
        }
        return result;
    }

    private void printStrings(final BufferedImage image, final Collection<String> lines) {
        final Graphics g = image.getGraphics();

        final Font font = new Font("Tahoma", Font.PLAIN, 18);
        g.setFont(font);
        g.setColor(Color.red);
        g.setFont(font);

        int y = 575;
        for (final String line : lines) {
            g.drawString(line, 25, y);
            y += 25;
        }
    }

    private String getFilenameFor(final String currentTest) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH-mm-ss", Locale.US);
        return String.format("%s%s_%s.png", getClass().getResource("/")
                .getFile(), currentTest, "(time_" + simpleDateFormat.format(new Date()) + ")");
    }

    private String getOriginalTestName(final String testName) {
        final Pattern pattern = Pattern.compile("(.+?)(\\.\\d+)?");
        final Matcher matcher = pattern.matcher(testName);
        if (matcher.matches()) {
            return matcher.group(1);
        }

        return testName;
    }
}
