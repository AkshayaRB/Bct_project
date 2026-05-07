package pages;

import base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object for Login page.
 * Contains locators and methods specific to login functionality.
 */
public class LoginPage extends BasePage {

    // Page locators using @FindBy annotations
    @FindBy(id = "username")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(className = "error-message")
    private WebElement errorMessage;

    @FindBy(className = "success-message")
    private WebElement successMessage;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Enter username
     */
    public void enterUsername(String username) {
        sendKeysToElement(usernameField, username);
    }

    /**
     * Enter password
     */
    public void enterPassword(String password) {
        sendKeysToElement(passwordField, password);
    }

    /**
     * Click login button
     */
    public void clickLoginButton() {
        clickElement(loginButton);
    }

    /**
     * Perform complete login action
     */
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    /**
     * Get error message text
     */
    public String getErrorMessage() {
        return getElementText(errorMessage);
    }

    /**
     * Check if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(errorMessage);
    }

    /**
     * Check if login was successful
     */
    public boolean isLoginSuccessful() {
        return isElementDisplayed(successMessage);
    }
}
