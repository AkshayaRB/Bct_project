package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for taking screenshots during test execution.
 */
public class ScreenshotUtil {
    private WebDriver driver;
    private static final String SCREENSHOT_DIR = "screenshots/";

    public ScreenshotUtil(WebDriver driver) {
        this.driver = driver;
        // Create screenshots directory if it doesn't exist
        new File(SCREENSHOT_DIR).mkdirs();
    }

    /**
     * Take screenshot and save to file
     */
    public String takeScreenshot(String fileName) {
        try {
            // Create filename with timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fullFileName = SCREENSHOT_DIR + fileName + "_" + timestamp + ".png";

            // Take screenshot
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // Save to file
            FileUtils.copyFile(screenshot, new File(fullFileName));

            return fullFileName;
        } catch (IOException e) {
            System.err.println("Failed to take screenshot: " + e.getMessage());
            return null;
        }
    }

    /**
     * Take screenshot with default filename
     */
    public String takeScreenshot() {
        return takeScreenshot("screenshot");
    }
}
