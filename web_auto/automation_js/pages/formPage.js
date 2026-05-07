class FormPage {
  constructor(page) {
    this.page = page;
    this.fullNameInput = page.locator('#full-name');
    this.emailInput = page.locator('#email');
    this.ageInput = page.locator('#age');
    this.phoneInput = page.locator('#phone');
    this.departmentSelect = page.locator('#department');
    this.termsCheckbox = page.locator('#terms');
    this.submitButton = page.locator('button[type="submit"]');
    this.successMessage = page.locator('.success-message');
  }

  async enterFullName(name) {
    await this.fullNameInput.fill(name);
  }

  async enterEmail(email) {
    await this.emailInput.fill(email);
  }

  async enterAge(age) {
    await this.ageInput.fill(age.toString());
  }

  async enterPhone(phone) {
    await this.phoneInput.fill(phone);
  }

  async selectDepartment(value) {
    await this.departmentSelect.selectOption(value);
  }

  async clickTerms() {
    const isChecked = await this.termsCheckbox.isChecked();
    if (!isChecked) {
      await this.termsCheckbox.click();
    }
  }

  async submitForm() {
    await this.submitButton.click();
  }

  async waitForSuccessMessage() {
    await this.successMessage.waitFor({ state: 'visible', timeout: 5000 });
    return true;
  }
}

module.exports = FormPage;
