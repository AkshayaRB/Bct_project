package tests;

import base.BaseTest;
import pages.LoginPage;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NavigationTests extends BaseTest {

    @Test(description = "Test navigation menu")
    public void testNavigation() {
        driver.get(config.getProperty("login.url"));
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(config.getProperty("valid.username"), config.getProperty("valid.password"));

        // Click on Student List
        driver.findElement(By.id("nav-table")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("table.html"));

        // Click on Dynamic Content
        driver.findElement(By.id("nav-dynamic")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("dynamic.html"));
    }
}
