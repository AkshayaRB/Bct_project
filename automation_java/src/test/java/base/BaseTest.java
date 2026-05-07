package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.LoggerUtil;
import utils.ScreenshotUtil;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

/**
 * Base test class containing WebDriver setup and teardown.
 * All test classes should extend this class.
 */
public class BaseTest {
    protected WebDriver driver;
    protected Properties config;
    protected LoggerUtil logger;
    protected ScreenshotUtil screenshotUtil;

    @BeforeMethod
    public void setUp() {
        // Load configuration
        config = new Properties();
        try {
            config.load(new FileInputStream("src/main/resources/config.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }

        // Initialize logger
        logger = new LoggerUtil();
        logger.info("Starting test setup");

        // Get browser from config
        String browser = config.getProperty("browser", "chrome").toLowerCase();
        boolean headless = Boolean.parseBoolean(config.getProperty("headless", "false"));

        logger.info("Initializing WebDriver for browser: " + browser);

        // Initialize WebDriver dynamically based on browser config
        try {
            switch (browser) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions chromeOptions = new ChromeOptions();
                    if (headless) chromeOptions.addArguments("--headless");
                    driver = new ChromeDriver(chromeOptions);
                    break;

                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    if (headless) firefoxOptions.addArguments("--headless");
                    driver = new FirefoxDriver(firefoxOptions);
                    break;

                case "edge":
                    WebDriverManager.edgedriver().setup();
                    EdgeOptions edgeOptions = new EdgeOptions();
                    if (headless) edgeOptions.addArguments("--headless");
                    driver = new EdgeDriver(edgeOptions);
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported browser: " + browser +
                        ". Supported browsers: chrome, firefox, edge");
            }
        } catch (Exception e) {
            logger.error("Failed to initialize WebDriver for browser: " + browser, e);
            throw new RuntimeException("WebDriver initialization failed", e);
        }

        // Set timeouts
        int implicitWait = Integer.parseInt(config.getProperty("implicit.wait", "10"));
        int pageLoadTimeout = Integer.parseInt(config.getProperty("page.load.timeout", "30"));

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
        driver.manage().window().maximize();

        // Initialize screenshot utility
        screenshotUtil = new ScreenshotUtil(driver);

        logger.info("WebDriver initialized successfully for browser: " + browser);
    }

    @AfterMethod
    public void tearDown() {
        logger.info("Starting test teardown");
        if (driver != null) {
            try {
                driver.quit();
                logger.info("WebDriver closed successfully");
            } catch (Exception e) {
                logger.error("Error during WebDriver cleanup", e);
            }
        }
    }

    /**
     * Navigate to base URL from config
     */
    protected void navigateToBaseUrl() {
        String baseUrl = config.getProperty("base.url");
        if (baseUrl != null && !baseUrl.isEmpty()) {
            driver.get(baseUrl);
            logger.info("Navigated to base URL: " + baseUrl);
        } else {
            logger.warn("Base URL not configured in config.properties");
        }
    }

    /**
     * Get current browser name
     */
    protected String getCurrentBrowser() {
        return config.getProperty("browser", "chrome");
    }
}
