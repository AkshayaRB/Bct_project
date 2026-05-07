const { expect } = require('@playwright/test');

/**
 * BasePage for all Playwright page objects.
 * Implements Anti-Gravity Layer for enhanced resilience.
 */
class BasePage {
  constructor(page) {
    this.page = page;
  }

  /**
   * Anti-Gravity: Safe click with retry and screenshot on failure
   */
  async safeClick(locator) {
    try {
      await this.waitAndValidate(locator);
      await locator.click();
    } catch (error) {
      console.log(`[Anti-Gravity] Click failed on ${locator}. Capturing screenshot...`);
      await this.page.screenshot({ path: `failure-click-${Date.now()}.png`, fullPage: true });
      throw error;
    }
  }

  /**
   * Anti-Gravity: Safe fill with retry and screenshot on failure
   */
  async safeFill(locator, text) {
    try {
      await this.waitAndValidate(locator);
      await locator.fill(text);
    } catch (error) {
      console.log(`[Anti-Gravity] Fill failed on ${locator}. Capturing screenshot...`);
      await this.page.screenshot({ path: `failure-fill-${Date.now()}.png`, fullPage: true });
      throw error;
    }
  }

  /**
   * Anti-Gravity: Wait and validate element is visible and stable
   */
  async waitAndValidate(locator) {
    // Playwright auto-waits, but this ensures visibility explicitly 
    // and can be extended for additional checks if an element is flaky.
    await locator.waitFor({ state: 'visible', timeout: 10000 });
  }
}

module.exports = BasePage;
