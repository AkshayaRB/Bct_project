package pages;

import base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class FormPage extends BasePage {

    @FindBy(id = "full-name")
    private WebElement fullNameInput;

    @FindBy(id = "department")
    private WebElement departmentSelect;

    @FindBy(id = "terms")
    private WebElement termsCheckbox;

    @FindBy(id = "role-student")
    private WebElement roleStudentRadio;

    @FindBy(id = "role-ta")
    private WebElement roleTaRadio;

    @FindBy(id = "submit-form")
    private WebElement submitButton;

    @FindBy(className = "success-message")
    private WebElement successMessage;

    public FormPage(WebDriver driver) {
        super(driver);
    }

    public void enterFullName(String name) {
        sendKeysToElement(fullNameInput, name);
    }

    public void selectDepartment(String value) {
        sendKeysToElement(departmentSelect, value); // Standard sendKeys works on <select> in some drivers, but standard Select is better.
    }

    public void clickTerms() {
        if (!termsCheckbox.isSelected()) {
            clickElement(termsCheckbox);
        }
    }

    public void selectRole(String role) {
        if (role.equalsIgnoreCase("ta")) {
            clickElement(roleTaRadio);
        } else {
            clickElement(roleStudentRadio);
        }
    }

    public void submitForm() {
        clickElement(submitButton);
    }

    public boolean isSuccessMessageDisplayed() {
        return isElementDisplayed(successMessage);
    }
}
