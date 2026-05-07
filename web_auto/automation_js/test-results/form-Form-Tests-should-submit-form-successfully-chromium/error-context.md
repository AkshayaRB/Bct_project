# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: form.spec.js >> Form Tests >> should submit form successfully
- Location: tests\form.spec.js:7:3

# Error details

```
Error: expect(received).toBeTruthy()

Received: false
```

# Page snapshot

```yaml
- generic [ref=e2]:
  - navigation [ref=e3]:
    - link "Dashboard" [ref=e4] [cursor=pointer]:
      - /url: dashboard.html
    - link "Data Form" [ref=e5] [cursor=pointer]:
      - /url: form.html
    - link "Dynamic Content" [ref=e6] [cursor=pointer]:
      - /url: dynamic.html
    - link "Student List" [ref=e7] [cursor=pointer]:
      - /url: table.html
    - link "Logout" [ref=e8] [cursor=pointer]:
      - /url: "#"
  - heading "Student Entry Form" [level=1] [ref=e9]
  - generic [ref=e10]: Student successfully added to the database!
  - generic [ref=e11]:
    - generic [ref=e12]:
      - generic [ref=e13]: Full Name
      - textbox "Full Name" [ref=e14]
    - generic [ref=e15]:
      - generic [ref=e16]: Email
      - textbox "Email" [ref=e17]
    - generic [ref=e18]:
      - generic [ref=e19]:
        - generic [ref=e20]: Age
        - spinbutton "Age" [ref=e21]
      - generic [ref=e22]:
        - generic [ref=e23]: Phone
        - textbox "Phone" [ref=e24]
    - generic [ref=e25]:
      - generic [ref=e26]: Department
      - combobox "Department" [ref=e27]:
        - option "Computer Science" [selected]
        - option "Electrical Engineering"
        - option "Mechanical Engineering"
        - option "Civil Engineering"
        - option "Mathematics"
    - generic [ref=e28]:
      - checkbox "I agree to the terms" [ref=e29]
      - generic [ref=e30]: I agree to the terms
    - generic [ref=e31]:
      - generic [ref=e32]: Role
      - generic [ref=e33]:
        - generic [ref=e34]:
          - radio "Student" [checked] [ref=e35]
          - text: Student
        - generic [ref=e36]:
          - radio "Teaching Assistant" [ref=e37]
          - text: Teaching Assistant
    - button "Save Record" [active] [ref=e38] [cursor=pointer]
```

# Test source

```ts
  1  | const { test, expect } = require('@playwright/test');
  2  | const LoginPage = require('../pages/loginPage');
  3  | const FormPage = require('../pages/formPage');
  4  | const config = require('../utils/config');
  5  | 
  6  | test.describe('Form Tests', () => {
  7  |   test('should submit form successfully', async ({ page }) => {
  8  |     // Login
  9  |     const loginPage = new LoginPage(page);
  10 |     await loginPage.navigate();
  11 |     await loginPage.login(config.credentials.valid.username, config.credentials.valid.password);
  12 | 
  13 |     // Navigate to form
  14 |     await page.goto('/form.html');
  15 |     const formPage = new FormPage(page);
  16 | 
  17 |     await formPage.enterFullName('Bob Test');
  18 |     await formPage.enterEmail('bob@example.com');
  19 |     await formPage.enterAge(25);
  20 |     await formPage.enterPhone('1234567890');
  21 |     await formPage.selectDepartment('ee');
  22 |     await formPage.clickTerms();
  23 |     await formPage.submitForm();
  24 | 
> 25 |     expect(await formPage.isSuccessMessageDisplayed()).toBeTruthy();
     |                                                        ^ Error: expect(received).toBeTruthy()
  26 |   });
  27 | });
  28 | 
```