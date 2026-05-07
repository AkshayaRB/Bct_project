package pages;

import base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object for Dashboard page.
 * Contains locators and methods for dashboard interactions.
 */
public class DashboardPage extends BasePage {

    @FindBy(tagName = "h1")
    private WebElement welcomeMessage;

    @FindBy(id = "logout-btn")
    private WebElement logoutButton;

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Get welcome message text
     */
    public String getWelcomeMessage() {
        return getElementText(welcomeMessage);
    }

    /**
     * Check if dashboard is loaded
     */
    public boolean isDashboardLoaded() {
        return isElementDisplayed(welcomeMessage);
    }

    /**
     * Click logout button
     */
    public void clickLogout() {
        clickElement(logoutButton);
    }
}
