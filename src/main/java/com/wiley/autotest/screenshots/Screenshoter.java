package com.wiley.autotest.screenshots;

//import net.jsourcerer.webdriver.jserrorcollector.JavaScriptError;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.*;
import org.testng.ITestResult;
import org.testng.Reporter;
import ru.yandex.qatools.allure.annotations.Attachment;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.wiley.autotest.screenshots.ScreenShotsPathsHolder.addScreenShotPathForTest;
import static com.wiley.autotest.selenium.SeleniumHolder.getWebDriver;
import static com.wiley.autotest.utils.TestUtils.getTestName;

public class Screenshoter {

    private static final double TRANSPARENCY_MASK_PERCENT = 85; //percent

    private static final int BRIGHTNESS = (int) (TRANSPARENCY_MASK_PERCENT * 255 / 100);
    private static final Color NON_ANALISE_REGION_COLOR = new Color(255, 220, 220, BRIGHTNESS);

    private static final Log LOGGER = LogFactory.getLog(Screenshoter.class);

    private enum CombinationType {
        ADDITION {
            @Override
            int getPixel(int srcPixel, int destPixel, int maskPixel) {
                return (maskPixel & 0x00ffffff) != 0 ? getColor(srcPixel) | 0xff000000 : destPixel;
            }
        },
        SUBSTRUCTION {
            @Override
            int getPixel(int srcPixel, int destPixel, int maskPixel) {
                return (maskPixel & 0x00ffffff) == 0 ? getColor(srcPixel) | 0xfe000000 : destPixel;
            }
        };

        private static int getColor(final int pixel) {
            return pixel & 0x00ffffff;
        }

        abstract int getPixel(final int srcPixel, final int destPixel, final int maskPixel);
    }

    public synchronized void takeScreenshot(final String errorMessage, final String testName) {
        try {
            final File screenShot = ((TakesScreenshot) getWebDriver()).getScreenshotAs(OutputType.FILE);
            final BufferedImage image = ImageIO.read(screenShot);
//This probably could be used someday (do not delete)
//            int jsErrorNumber = JavaScriptError.readErrors(getWebDriver()).size();
//            printStrings(image, removeNL(testName, errorMessage, "The following number of JS errors appeared during the test: " + jsErrorNumber));
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

    @Attachment(value = "{0} : {1}", type = "image/png")
    public byte[] attachScreenShotToAllure(final String testName, final String errorMessage, final File screenShot) throws IOException {
        return Files.readAllBytes(screenShot.toPath());
    }

    public void takeScreenshotForComparison(final String path, final String name) {
        try {
            final File screenShot = ((TakesScreenshot) getWebDriver()).getScreenshotAs(OutputType.FILE);
            final BufferedImage image = ImageIO.read(screenShot);

            final String pathName = path + "\\" + name;
            ImageIO.write(image, "png", new File(pathName));
        } catch (IOException e) {
            LOGGER.error("IOException occurs", e);
        }
    }

    public void takeScreenshot(final ITestResult testResult) {
        final Throwable testResultThrowable = testResult.getThrowable();
        String message = testResultThrowable.getMessage() != null ? testResultThrowable.getMessage() :
                testResultThrowable.getCause().getMessage();

        if (message == null) {
            message = "Test failed";
        }

        takeScreenshot(message, getTestName(testResult));
    }

    /*
    *  The method extracts matched image for comparison from screenshot image using inner mask.
    */
    public static BufferedImage getMatchedImage(final BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage matchedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int[] imagePixels = image.getRGB(0, 0, width, height, null, 0, width);
        for (int i = 0; i < imagePixels.length; i++) {
            if (((imagePixels[i] >> 24) & 0xff) < 255) {
                imagePixels[i] = Color.WHITE.getRGB();
            }
        }
        matchedImage.setRGB(0, 0, width, height, imagePixels, 0, width);
        return matchedImage;
    }

    public static BufferedImage readImage(final File file) {
        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            LOGGER.error("IOException occurs during reading", e);
        }
        return null;
    }

    public static void writeImage(final File file, final BufferedImage image) {
        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            LOGGER.error("IOException occurs during writing", e);
        }
    }

    /*
    * The method returns screenshot of whole screen
    */
    public static BufferedImage takeScreenshot() {
        try {
            return ImageIO.read(new ByteArrayInputStream(((TakesScreenshot) getWebDriver()).getScreenshotAs(OutputType.BYTES)));
        } catch (IOException e) {
            LOGGER.error("IOException occurs", e);
        }
        return null;
    }

    /*
    * The method returns screenshot of screen with a mask included only particular elements and excluded other elements.
    * Before operation there provide substitution of defined elements.
    */
    public static BufferedImage takeScreenshot(final ScreenshotProvider provider) {
        WebDriver driver = getWebDriver();
        WebDriver.Window window = driver.manage().window();
        org.openqa.selenium.Dimension sizeBefore = window.getSize();
        Map<WebElement, String> previousStateMap = substituteElements(provider.getSubstitutions());
        provider.beforeScreenshot(driver);
        BufferedImage image = takeScreenshot();
        if (provider.getIncludeLocators() != null || provider.getExcludeLocators() != null) {
            try {
                image = createScreenshotImage(image, createMask(image, provider.getIncludeLocators(), provider.getExcludeLocators()));
            } catch (IOException e) {
                LOGGER.error("IOException occurs", e);
            }
        }
        if (image != null) {
            image = provider.cut(driver, image);
        }
        provider.afterScreenshot(driver);
        restoreSubstitutedElements(previousStateMap);
        org.openqa.selenium.Dimension sizeAfter = window.getSize();
        if (sizeBefore.width != sizeAfter.width || sizeBefore.height != sizeAfter.height) {
            window.setSize(new org.openqa.selenium.Dimension(sizeBefore.width, sizeBefore.height));
        }
        return image;
    }

    /*
    * The method inserts elements represented by locators into image and returns result image
    */
    public static BufferedImage insertElementsToImage(final BufferedImage image, final InsertProvider provider) {
        if (image != null && provider.getInsertedLocators() != null) {
            BufferedImage src = takeScreenshot(provider);
            BufferedImage mask = createMask(image, provider.getInsertedLocators(), null);
            if (src != null && mask != null) {
                return getCombinedImage(src, image, mask, CombinationType.ADDITION);
            }
        }
        return image;
    }

    /*
    * The method changes innerHTML for particular elements defined by substitutions.
    */
    private static Map<WebElement, String> substituteElements(final List<Substitution> substitutions) {
        if (substitutions != null) {
            Map<WebElement, String> previousStateMap = new HashMap<WebElement, String>();
            final WebDriver webDriver = getWebDriver();
            for (Substitution substitution : substitutions) {
                for (WebElement element : (List<WebElement>) substitution.getWebElements(webDriver)) {
                    try {
                        previousStateMap.put(element, element.getAttribute("innerHTML"));
                        Substitution.substitute(webDriver, element, substitution.getHTML(element));
                    } catch (Exception e) {
                        LOGGER.error("JDOM object cannot be created for a substitution", e);
                    }
                }
            }
            return previousStateMap;
        }
        return null;
    }

    private static void restoreSubstitutedElements(Map<WebElement, String> previousStateMap) {
        if (previousStateMap != null) {
            final WebDriver webDriver = getWebDriver();
            for (Map.Entry<WebElement, String> entry : previousStateMap.entrySet()) {
                Substitution.substitute(webDriver, entry.getKey(), entry.getValue());
            }
        }
    }

    /*
    *  The method creates a mask for the image defined by included and excluded elements.
    */
    private static BufferedImage createMask(final BufferedImage image, final List<ScreenshotLocator> include, final List<ScreenshotLocator> exclude) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage mask = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D gMask = mask.createGraphics();

        if (include == null) {
            gMask.setColor(Color.WHITE);
            gMask.fillRect(0, 0, width, height);
        } else {
            gMask.setColor(Color.BLACK);
            gMask.fillRect(0, 0, width, height);
            gMask.setColor(Color.WHITE);
            drawRectangles(include, gMask, width, height);
        }
        if (exclude != null) {
            gMask.setColor(Color.BLACK);
            drawRectangles(exclude, gMask, width, height);
        }
        gMask.dispose();
        return mask;
    }

    /*
    *  The method creates a result screenshot image. The mask will be combined with the input image.
    */
    private static BufferedImage createScreenshotImage(final BufferedImage image, final BufferedImage mask) throws IOException {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage src = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = src.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.setColor(NON_ANALISE_REGION_COLOR);
        g.fillRect(0, 0, width, height);
        g.dispose();

        return getCombinedImage(src, image, mask, CombinationType.SUBSTRUCTION);
    }

    /*
    *  The method combines a image from the source, the destination and the mask. The type defines a method of application of mask.
    */
    private static BufferedImage getCombinedImage(final BufferedImage src, final BufferedImage dest,
                                                  final BufferedImage mask, final CombinationType type) {
        int width = dest.getWidth();
        int height = dest.getHeight();

        int[] srcPixels = src.getRGB(0, 0, width, height, null, 0, width);
        int[] destPixels = dest.getRGB(0, 0, width, height, null, 0, width);
        int[] maskPixels = mask.getRGB(0, 0, width, height, null, 0, width);

        for (int i = 0; i < destPixels.length; i++) {
            destPixels[i] = type.getPixel(srcPixels[i], destPixels[i], maskPixels[i]);
        }
        dest.setRGB(0, 0, width, height, destPixels, 0, width);
        return dest;
    }

    private static void drawRectangles(final List<ScreenshotLocator> locators, final Graphics2D gMask,
                                       final int width, final int height) {
        final WebDriver webDriver = getWebDriver();
        for (ScreenshotLocator screenshotLocator : locators) {
            for (WebElement element : webDriver.findElements(screenshotLocator.locator)) {
                if (element.isDisplayed()) {
                    Rectangle rectangle = screenshotLocator.getRectangle(element);
                    if (onImage(rectangle.getLocation(), rectangle.getSize(), width, height)) {
                        gMask.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                    }
                }
            }
        }
    }

    private static boolean onImage(final Point location, final Dimension dimension, final int width, final int height) {
        return !((location.x + dimension.width) <= 0 || location.x > width || (location.y + dimension.height) <= 0 || location.y > height);
    }

    private static Collection<String> removeNL(final String... strings) {
        final Collection<String> result = new ArrayList<String>();
        for (final String each : strings) {
            if (each != null) {
                result.addAll(Arrays.asList(each.split("\n")));
            }
        }
        return result;
    }

    private static void printStrings(final BufferedImage image, final Collection<String> lines) {
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
        return String.format("%s%s_%s.png", getClass().getResource("/").getFile(), currentTest, "(time_" + simpleDateFormat.format(new Date()) + ")");
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
