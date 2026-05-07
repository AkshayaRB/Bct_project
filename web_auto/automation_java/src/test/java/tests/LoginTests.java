package tests;

import base.BaseTest;
import pages.LoginPage;
import pages.DashboardPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test class for login functionality.
 * Contains test cases for valid and invalid login scenarios.
 */
public class LoginTests extends BaseTest {

    @Test(description = "Test valid login functionality")
    public void testValidLogin() {
        logger.info("Starting valid login test");

        // Navigate to login page
        driver.get(config.getProperty("login.url"));

        // Initialize page objects
        LoginPage loginPage = new LoginPage(driver);

        // Perform login with valid credentials
        String validUsername = config.getProperty("valid.username");
        String validPassword = config.getProperty("valid.password");

        loginPage.login(validUsername, validPassword);

        // Verify login success
        DashboardPage dashboardPage = new DashboardPage(driver);
        Assert.assertTrue(dashboardPage.isDashboardLoaded(),
            "Dashboard should be loaded after successful login");

        // Take screenshot for verification
        screenshotUtil.takeScreenshot("valid_login_success");

        logger.info("Valid login test completed successfully");
    }

    @Test(description = "Test invalid login functionality")
    public void testInvalidLogin() {
        logger.info("Starting invalid login test");

        // Navigate to login page
        driver.get(config.getProperty("login.url"));

        // Initialize page objects
        LoginPage loginPage = new LoginPage(driver);

        // Perform login with invalid credentials
        String invalidUsername = config.getProperty("invalid.username");
        String invalidPassword = config.getProperty("invalid.password");

        loginPage.login(invalidUsername, invalidPassword);

        // Verify error message is displayed
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
            "Error message should be displayed for invalid login");

        // Take screenshot for verification
        screenshotUtil.takeScreenshot("invalid_login_error");

        logger.info("Invalid login test completed successfully");
    }
}
