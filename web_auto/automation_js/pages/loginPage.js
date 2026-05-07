const { expect } = require('@playwright/test');
const BasePage = require('./BasePage');

/**
 * Page Object Model for Login page
 * Encapsulates login page elements and actions
 */
class LoginPage extends BasePage {
  constructor(page) {
    super(page);

    // Page elements
    this.usernameField = page.locator('#username');
    this.passwordField = page.locator('#password');
    this.loginButton = page.locator('#login-button');
    this.errorMessage = page.locator('.error-message');
    this.successMessage = page.locator('.success-message');
  }

  /**
   * Navigate to login page
   */
  async navigate() {
    await this.page.goto('/index.html');
  }

  /**
   * Enter username
   */
  async enterUsername(username) {
    await this.safeFill(this.usernameField, username);
  }

  /**
   * Enter password
   */
  async enterPassword(password) {
    await this.safeFill(this.passwordField, password);
  }

  /**
   * Click login button
   */
  async clickLoginButton() {
    await this.safeClick(this.loginButton);
  }

  /**
   * Perform complete login action
   */
  async login(username, password) {
    await this.enterUsername(username);
    await this.enterPassword(password);
    await this.clickLoginButton();
  }

  /**
   * Get error message text
   */
  async getErrorMessage() {
    return await this.errorMessage.textContent();
  }

  /**
   * Check if error message is visible
   */
  async isErrorMessageVisible() {
    return await this.errorMessage.isVisible();
  }

  /**
   * Check if login was successful
   */
  async isLoginSuccessful() {
    return await this.successMessage.isVisible();
  }

  /**
   * Wait for login page to load
   */
  async waitForPageLoad() {
    await this.page.waitForLoadState('networkidle');
    await expect(this.usernameField).toBeVisible();
  }
}

module.exports = LoginPage;
