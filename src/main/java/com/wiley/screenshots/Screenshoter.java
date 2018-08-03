package com.wiley.screenshots;

import com.wiley.config.Configuration;
import com.wiley.holders.DriverHolder;
import io.qameta.allure.Attachment;
import org.openqa.selenium.Alert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnhandledAlertException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: ntyukavkin
 * Date: 10.04.2018
 * Time: 17:30
 */
public class Screenshoter {

    @Attachment(value = "{0} : {1}", type = "image/png")
    public byte[] attachScreenShotToAllure(final String testName, final String errorMessage, final File screenShot) throws IOException {
        return Files.readAllBytes(screenShot.toPath());
    }

    public String takeScreenshot(final String errorMessage, final String testName) {
        try {
            BufferedImage image;
            if (Configuration.browser.equals("chrome")) {
                image = ImageIO.read(new ChromeScreenshoter().getFullScreenshotAs(OutputType.FILE));
            } else {
                image = ImageIO.read(((TakesScreenshot)DriverHolder.getDriver()).getScreenshotAs(OutputType.FILE));
            }

            printStrings(image, removeNL(testName, errorMessage));

            final String pathName = getFilenameFor(testName);
            final File screenShotWithProjectPath = new File(pathName);
            ImageIO.write(image, "png", screenShotWithProjectPath);
            attachScreenShotToAllure(errorMessage, testName, screenShotWithProjectPath);
            return screenShotWithProjectPath.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } catch (UnhandledAlertException alertException) {
            Alert alert = DriverHolder.getDriver().switchTo().alert();
            alert.dismiss();
            return takeScreenshot(errorMessage, testName);
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
        return String.format("%s%s_%s_%s.png", getClass().getResource("/").getFile(), currentTest, "" + simpleDateFormat.format(new Date()), UUID.randomUUID());
    }
}
