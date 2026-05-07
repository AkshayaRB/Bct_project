const { test, expect } = require('@playwright/test');
const LoginPage = require('../pages/loginPage');
const config = require('../utils/config');

test.describe('Navigation Tests', () => {
  test('should navigate between pages', async ({ page }) => {
    // Login
    const loginPage = new LoginPage(page);
    await loginPage.navigate();
    await loginPage.login(config.credentials.valid.username, config.credentials.valid.password);

    // Click on Student List
    await page.click('#nav-table');
    await expect(page).toHaveURL(/table.html/);

    // Click on Dynamic Content
    await page.click('#nav-dynamic');
    await expect(page).toHaveURL(/dynamic.html/);
  });
});
