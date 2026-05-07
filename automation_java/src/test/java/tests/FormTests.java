package tests;

import base.BaseTest;
import pages.FormPage;
import pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FormTests extends BaseTest {

    @Test(description = "Test form submission")
    public void testFormSubmission() {
        // Login first
        driver.get(config.getProperty("login.url"));
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(config.getProperty("valid.username"), config.getProperty("valid.password"));

        // Navigate to form page
        driver.get(config.getProperty("base.url") + "/form.html");

        FormPage formPage = new FormPage(driver);
        formPage.enterFullName("Alice Test");
        formPage.selectDepartment("cs");
        formPage.clickTerms();
        formPage.selectRole("ta");
        formPage.submitForm();

        Assert.assertTrue(formPage.isSuccessMessageDisplayed(), "Success message should be displayed");
    }
}
