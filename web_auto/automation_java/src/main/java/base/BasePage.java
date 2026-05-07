package base;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.PageFactory;
import java.time.Duration;

/**
 * Base class for all Page Objects.
 * Contains common WebDriver actions and utilities with Anti-Gravity Layer resilience.
 */
public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    private static final int MAX_RETRIES = 3;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        // Initialize WebDriverWait with 10 seconds timeout
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Initialize PageFactory elements
        PageFactory.initElements(driver, this);
    }

    /**
     * Anti-Gravity: Smart Click with retries
     */
    protected void smartClick(WebElement element) {
        int attempt = 0;
        while (attempt < MAX_RETRIES) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(element)).click();
                return; // Success
            } catch (StaleElementReferenceException | NoSuchElementException e) {
                attempt++;
                System.out.println("[Anti-Gravity] Click failed, retrying... (" + attempt + "/" + MAX_RETRIES + ")");
                if (attempt == MAX_RETRIES) {
                    System.err.println("[Anti-Gravity] Failed to click element after " + MAX_RETRIES + " attempts: " + e.getMessage());
                    throw e;
                }
                sleepSilently(500); // Wait before retrying
            }
        }
    }

    /**
     * Anti-Gravity: Smart SendKeys with retries
     */
    protected void smartSendKeys(WebElement element, String text) {
        int attempt = 0;
        while (attempt < MAX_RETRIES) {
            try {
                wait.until(ExpectedConditions.visibilityOf(element)).clear();
                element.sendKeys(text);
                return; // Success
            } catch (StaleElementReferenceException | NoSuchElementException e) {
                attempt++;
                System.out.println("[Anti-Gravity] SendKeys failed, retrying... (" + attempt + "/" + MAX_RETRIES + ")");
                if (attempt == MAX_RETRIES) {
                    System.err.println("[Anti-Gravity] Failed to send keys after " + MAX_RETRIES + " attempts: " + e.getMessage());
                    throw e;
                }
                sleepSilently(500);
            }
        }
    }

    /**
     * Anti-Gravity: Smart Wait for Element with retries
     */
    protected WebElement smartWaitForElement(WebElement element) {
        int attempt = 0;
        while (attempt < MAX_RETRIES) {
            try {
                return wait.until(ExpectedConditions.visibilityOf(element));
            } catch (StaleElementReferenceException | NoSuchElementException e) {
                attempt++;
                System.out.println("[Anti-Gravity] WaitForElement failed, retrying... (" + attempt + "/" + MAX_RETRIES + ")");
                if (attempt == MAX_RETRIES) {
                    System.err.println("[Anti-Gravity] Failed to wait for element after " + MAX_RETRIES + " attempts: " + e.getMessage());
                    throw e;
                }
                sleepSilently(500);
            }
        }
        return null;
    }

    private void sleepSilently(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Click element after waiting for it to be clickable
     */
    protected void clickElement(WebElement element) {
        smartClick(element);
    }

    /**
     * Send keys to element after waiting for visibility
     */
    protected void sendKeysToElement(WebElement element, String text) {
        smartSendKeys(element, text);
    }

    /**
     * Get text from element after waiting for visibility
     */
    protected String getElementText(WebElement element) {
        WebElement safeElement = smartWaitForElement(element);
        return safeElement != null ? safeElement.getText() : "";
    }

    /**
     * Check if element is displayed
     */
    protected boolean isElementDisplayed(WebElement element) {
        try {
            return smartWaitForElement(element) != null && element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Wait for element to be visible
     */
    protected WebElement waitForElement(WebElement element) {
        return smartWaitForElement(element);
    }
}
