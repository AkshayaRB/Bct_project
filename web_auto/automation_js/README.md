# JavaScript Playwright Automation Framework

A modern web automation framework using Playwright with Page Object Model (POM) design pattern.

## Prerequisites

- **Node.js**: 16.x or higher
- **npm**: 7.0 or higher
- **Browser**: Chromium, Firefox, WebKit (auto-installed by Playwright)

## Installation

### 1. Verify Node.js Installation
```bash
node --version    # Should be 16.0.0 or higher
npm --version     # Should be 7.0.0 or higher
```

### 2. Install Project Dependencies
```bash
cd automation_js
npm install
```

This will install:
- @playwright/test (1.40.0)

### 3. Install Playwright Browsers
```bash
npx playwright install
```

This downloads:
- Chromium
- Firefox
- WebKit (Safari)

## Project Structure

```
automation_js/
├── package.json                     # Node.js dependencies
├── playwright.config.js             # Playwright configuration
├── tests/
│   └── login.spec.js               # Test specifications
├── pages/
│   └── loginPage.js                # Login page object
├── utils/
│   └── config.js                   # Configuration file
├── playwright-report/              # HTML test reports (generated)
├── test-results/                   # Test results (generated)
└── README.md                        # This file
```

## Configuration

### Environment Variables

Create a `.env` file or export variables:

```bash
# macOS/Linux
export BASE_URL=https://example.com
export VALID_USERNAME=testuser
export VALID_PASSWORD=testpass
export HEADLESS=true

# Windows (PowerShell)
$env:BASE_URL="https://example.com"
$env:VALID_USERNAME="testuser"
$env:VALID_PASSWORD="testpass"
$env:HEADLESS="true"

# Windows (CMD)
set BASE_URL=https://example.com
set VALID_USERNAME=testuser
set VALID_PASSWORD=testpass
set HEADLESS=true
```

### Configuration File (utils/config.js)

```javascript
const config = {
  urls: {
    base: process.env.BASE_URL || 'https://example.com',
    login: process.env.LOGIN_URL || 'https://example.com/login',
  },
  credentials: {
    valid: {
      username: process.env.VALID_USERNAME || 'testuser',
      password: process.env.VALID_PASSWORD || 'testpass',
    },
  },
  timeouts: {
    navigation: 30000,
    element: 10000,
  },
};
```

### playwright.config.js

Key configuration options:

```javascript
{
  testDir: './tests',           // Test files directory
  fullyParallel: true,          // Run tests in parallel
  retries: 0,                   // Retries per test
  workers: undefined,           // Number of workers
  use: {
    baseURL: process.env.BASE_URL || 'https://example.com',
    trace: 'on-first-retry',   // Tracing on failure
    screenshot: 'only-on-failure',
    video: 'retain-on-failure',
  },
  projects: [                   # Browser configurations
    'chromium',
    'firefox',
    'webkit',
    'Mobile Chrome',
    'Mobile Safari',
  ],
}
```

## Core Components

### Page Object (pages/loginPage.js)

```javascript
class LoginPage {
  constructor(page) {
    this.page = page;
    this.usernameField = page.locator('#username');
    this.passwordField = page.locator('#password');
    this.loginButton = page.locator('#login-button');
    this.errorMessage = page.locator('.error-message');
  }

  async login(username, password) {
    // Implementation
  }
}
```

**Available Methods**:
- `navigate()` - Navigate to login page
- `login(username, password)` - Complete login flow
- `enterUsername(username)` - Enter username
- `enterPassword(password)` - Enter password
- `clickLoginButton()` - Click login button
- `getErrorMessage()` - Get error text
- `isErrorMessageVisible()` - Check error visibility

### Test Specification (tests/login.spec.js)

```javascript
test.describe('Login Tests', () => {
  test.beforeEach(async ({ page }) => {
    // Setup before each test
  });

  test('should login successfully', async ({ page }) => {
    // Test implementation
  });
});
```

## Running Tests

### Run All Tests (All Browsers)
```bash
npm test
```

Runs tests on:
- Chromium
- Firefox
- WebKit
- Mobile Chrome
- Mobile Safari

### Run Specific Browser
```bash
# Chromium only
npx playwright test --project=chromium

# Firefox only
npx playwright test --project=firefox

# WebKit (Safari)
npx playwright test --project=webkit

# Mobile
npx playwright test --project="Mobile Chrome"
```

### Run Specific Test File
```bash
npx playwright test login.spec.js
```

### Run Specific Test
```bash
npx playwright test -g "should login successfully"
```

### Run in Headed Mode (Visible Browser)
```bash
npm run test:headed
```

### Run in Debug Mode
```bash
npm run test:debug
```

Opens Playwright Inspector for step-by-step execution.

### Run with Environment Variables
```bash
# macOS/Linux
BASE_URL=https://myapp.com VALID_USERNAME=user1 npm test

# Windows (PowerShell)
$env:BASE_URL="https://myapp.com"; npm test

# Windows (CMD)
set BASE_URL=https://myapp.com && npm test
```

### Generate Report
```bash
npm run report
```

Opens HTML report at `playwright-report/index.html`

## Test Cases

### login.spec.js Tests

#### Test 1: Valid Login
```
Test: should login successfully with valid credentials
- Navigate to login page
- Enter valid credentials
- Click login button
- Verify dashboard loads
- Verify URL contains 'dashboard'
```

#### Test 2: Invalid Login
```
Test: should show error message with invalid credentials
- Navigate to login page
- Enter invalid credentials
- Click login button
- Verify error message is visible
- Verify user stays on login page
```

#### Test 3: Required Fields
```
Test: should validate required fields
- Click login without entering credentials
- Verify field validation errors
```

#### Test 4: Network Error
```
Test: should handle network errors gracefully
- Mock network failure
- Attempt login
- Verify error handling
```

## Features

### Auto-Waiting
Playwright automatically waits for elements:

```javascript
// Waits for element visibility automatically
await page.click('#button');
await page.fill('#input', 'text');
```

### Locators
Multiple selector strategies:

```javascript
// CSS selector
page.locator('#id')
page.locator('.class')
page.locator('[data-test="value"]')

// XPath
page.locator('//button[text()="Click"]')

// Text content
page.locator(':has-text("Login")')
```

### Assertions
```javascript
// Visibility
await expect(element).toBeVisible();
await expect(element).toBeHidden();

// State
await expect(element).toBeEnabled();
await expect(element).toBeDisabled();

// Content
await expect(element).toContainText('text');
await expect(element).toHaveValue('value');

// Navigation
await expect(page).toHaveURL(/dashboard/);
```

### Screenshots
```javascript
// Screenshot on failure (automatic)
// Manual screenshot
await page.screenshot({ path: 'my-screenshot.png' });
```

### Video Recording
```javascript
// Enabled in config for failed tests
// View in test-results/*/video.webm
```

### Tracing
```javascript
// Enabled in config on first retry
// View trace:
// npx playwright show-trace test-results/*/trace.zip
```

## POM (Page Object Model) Pattern

### Creating New Page Objects

1. **Create page file** in `pages/`:
```javascript
// pages/myPage.js
class MyPage {
  constructor(page) {
    this.page = page;
    this.element1 = page.locator('#element1');
    this.element2 = page.locator('.element2');
  }

  async navigate() {
    await this.page.goto('/mypage');
  }

  async clickElement1() {
    await this.element1.click();
  }

  async getElement2Text() {
    return await this.element2.textContent();
  }
}

module.exports = MyPage;
```

2. **Use in tests**:
```javascript
const MyPage = require('../pages/myPage');

test('my test', async ({ page }) => {
  const myPage = new MyPage(page);
  await myPage.navigate();
  await myPage.clickElement1();
  // Assert
});
```

## Adding New Tests

1. **Create test file** in `tests/`:
```javascript
// tests/myFeature.spec.js
const { test, expect } = require('@playwright/test');
const MyPage = require('../pages/myPage');
const config = require('../utils/config');

test.describe('My Feature Tests', () => {
  test('should do something', async ({ page }) => {
    const myPage = new MyPage(page);
    await myPage.navigate();
    // Your test logic
    await expect(myPage.element).toBeVisible();
  });
});
```

2. **Run test**:
```bash
npx playwright test myFeature.spec.js
```

## Configuration Examples

### Custom Base URL
```bash
export BASE_URL=https://staging.example.com
npm test
```

### Headless Mode
```bash
export HEADLESS=true
npm test
```

### Slow Motion (Debug)
```bash
export SLOW_MO=3000
npm run test:headed
```

### CI Environment
```bash
npm test -- --reporter=json
```

## Debugging

### Playwright Inspector
```bash
npm run test:debug
```

### Console Logs
```javascript
test('my test', async ({ page }) => {
  console.log('Before login');
  await loginPage.login(user, pass);
  console.log('After login');
  page.on('console', msg => console.log(msg.text()));
});
```

### Screenshots on Demand
```javascript
test('my test', async ({ page }) => {
  await page.screenshot({ path: 'step1.png' });
  await page.fill('#input', 'text');
  await page.screenshot({ path: 'step2.png' });
});
```

### Pause Execution
```javascript
test('my test', async ({ page }) => {
  await page.pause();  // Inspector will open
});
```

## Running Tests in CI/CD

### GitHub Actions
```yaml
- name: Install dependencies
  run: npm install

- name: Install Playwright browsers
  run: npx playwright install

- name: Run tests
  run: npm test

- name: Upload report
  uses: actions/upload-artifact@v3
  if: always()
  with:
    name: playwright-report
    path: playwright-report/
```

### Jenkins
```groovy
stage('Test') {
    steps {
        dir('automation_js') {
            sh 'npm install'
            sh 'npx playwright install'
            sh 'npm test'
        }
    }
}
```

## Best Practices

1. **Use Page Objects**: Encapsulate page elements
2. **Meaningful Names**: Test names describe what they test
3. **Auto-Wait**: Trust Playwright's built-in waiting
4. **Explicit Over Implicit**: Use expect() for assertions
5. **No Hard Waits**: Avoid sleep() unless necessary
6. **Independent Tests**: Tests don't depend on each other
7. **Clean Data**: Use config for test data
8. **Descriptive Errors**: Clear assertion messages

## Troubleshooting

### Browser Not Found
```bash
npx playwright install
```

### Timeout Errors
```
Timeout waiting for element
- Increase timeouts in config.js
- Verify selectors are correct
- Check element visibility
```

### Authentication Issues
```
Login fails in tests but works manually
- Verify credentials in config.js
- Check if session is properly handled
- Look at screenshots/traces
```

### CI Test Failures
```
Tests pass locally but fail in CI
- Run in headed mode locally
- Check console logs
- Verify environment variables
```

## Reporting

### HTML Report
```bash
npm run report
```

Located at: `playwright-report/index.html`

Contains:
- Test results
- Screenshots on failure
- Videos on failure
- Execution time

### JSON Report
```bash
npx playwright test --reporter=json
```

Output: `test-results.json`

### Test Results
Location: `test-results/` directory

Contains:
- Screenshots (on failure)
- Videos (on failure)
- Traces (on first retry)

## Performance Tips

1. **Parallel Execution**: Runs by default
2. **Headless Mode**: Faster than headed
3. **Selective Browsers**: Don't run all if not needed
4. **Retry Strategy**: Balance between reliability and speed
5. **Resource Optimization**: Limit number of workers

## Resources

- [Playwright Documentation](https://playwright.dev/)
- [Playwright Testing](https://playwright.dev/docs/api/class-test)
- [Locators Guide](https://playwright.dev/docs/locators)
- [Assertions](https://playwright.dev/docs/test-assertions)
- [GitHub Repository](https://github.com/microsoft/playwright)

## Support

For issues:
1. Check `playwright-report/` for details
2. Review test results and traces
3. Check console output for errors
4. Enable debug mode: `npm run test:debug`

---

**Version**: 1.0
**Last Updated**: April 2026
