const { test, expect } = require('@playwright/test');
const LoginPage = require('../pages/loginPage');
const config = require('../utils/config');

/**
 * Login functionality test suite
 * Tests both valid and invalid login scenarios
 */
test.describe('Login Tests', () => {
  let loginPage;

  test.beforeEach(async ({ page }) => {
    loginPage = new LoginPage(page);
    await loginPage.navigate();
    await loginPage.waitForPageLoad();
  });

  test('should login successfully with valid credentials', async ({ page }) => {
    // Perform login with valid credentials
    await loginPage.login(
      config.credentials.valid.username,
      config.credentials.valid.password
    );

    // Verify successful login
    await expect(page.locator('h1')).toHaveText('Dashboard');

    // Additional verification - check URL
    await expect(page).toHaveURL(/dashboard/);
  });

  test('should show error message with invalid credentials', async ({ page }) => {
    // Perform login with invalid credentials
    await loginPage.login(
      config.credentials.invalid.username,
      config.credentials.invalid.password
    );

    // Verify error message is displayed
    await expect(loginPage.errorMessage).toBeVisible();

    // Verify specific error message content
    const errorText = await loginPage.getErrorMessage();
    expect(errorText).toContain('Invalid credentials');
  });
});
