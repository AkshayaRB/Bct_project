const { test, expect } = require('@playwright/test');
const LoginPage = require('../pages/loginPage');
const FormPage = require('../pages/formPage');
const config = require('../utils/config');

test.describe('Form Tests', () => {
  test('should submit form successfully', async ({ page }) => {
    // Login
    const loginPage = new LoginPage(page);
    await loginPage.navigate();
    await loginPage.login(config.credentials.valid.username, config.credentials.valid.password);

    // Navigate to form
    await page.goto('/form.html');
    const formPage = new FormPage(page);

    await formPage.enterFullName('Bob Test');
    await formPage.enterEmail('bob@example.com');
    await formPage.enterAge(25);
    await formPage.enterPhone('1234567890');
    await formPage.selectDepartment('ee');
    await formPage.clickTerms();
    await formPage.submitForm();

    expect(await formPage.waitForSuccessMessage()).toBeTruthy();
  });
});
