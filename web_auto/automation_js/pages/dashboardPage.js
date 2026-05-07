class DashboardPage {
  constructor(page) {
    this.page = page;
    this.welcomeMessage = page.locator('h1');
    this.logoutButton = page.locator('#logout-btn');
  }

  async clickLogout() {
    await this.logoutButton.click();
  }

  async isDashboardLoaded() {
    return await this.welcomeMessage.isVisible();
  }
}

module.exports = DashboardPage;
