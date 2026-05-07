# Java Selenium Automation Framework

A comprehensive Selenium WebDriver automation framework with TestNG, featuring Page Object Model (POM) design pattern.

## Prerequisites

- **Java**: JDK 11 or higher
- **Maven**: 3.6 or higher
- **Browser**: Chrome, Firefox, or Edge
- **IDE**: IntelliJ IDEA or Eclipse (optional)

## Installation

### 1. Install Java
```bash
# Verify Java installation
java -version
```

### 2. Install Maven
```bash
# Verify Maven installation
mvn -version
```

### 3. Install Project Dependencies
```bash
cd automation_java
mvn clean install
```

This will download:
- Selenium WebDriver 4.15.0
- TestNG 7.8.0
- Log4j2 2.20.0
- WebDriverManager 5.5.3
- Apache Commons IO 2.11.0

## Project Structure

```
automation_java/
├── pom.xml                          # Maven configuration
├── testng.xml                       # TestNG suite configuration
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── base/
│   │   │   │   ├── BasePage.java   # Common page actions
│   │   │   │   └── BaseTest.java   # WebDriver setup/teardown
│   │   │   ├── pages/
│   │   │   │   ├── LoginPage.java
│   │   │   │   └── DashboardPage.java
│   │   │   └── utils/
│   │   │       ├── LoggerUtil.java
│   │   │       └── ScreenshotUtil.java
│   │   └── resources/
│   │       ├── config.properties   # Configuration file
│   │       └── log4j2.xml         # Logging configuration
│   └── test/
│       └── java/
│           └── tests/
│               └── LoginTests.java
├── target/                          # Build output directory
├── logs/                            # Log files
└── screenshots/                     # Screenshot captures
```

## Configuration

### config.properties

Located at: `src/main/resources/config.properties`

```properties
# Browser configuration
browser=chrome              # Options: chrome, firefox, edge
headless=false             # Set to true for headless mode

# Application URLs
base.url=https://example.com
login.url=https://example.com/login

# Timeouts (in seconds)
implicit.wait=10
explicit.wait=15
page.load.timeout=30

# Test data
valid.username=testuser
valid.password=testpass
invalid.username=invalid
invalid.password=invalid
```

### Supported Browsers

| Browser | Config Value |
|---------|-------------|
| Chrome | `chrome` |
| Firefox | `firefox` |
| Edge | `edge` |

## Core Components

### BasePage.java
Base class for all page objects. Provides common WebDriver actions:

```java
// Methods available to all pages:
clickElement(element)           // Click with wait
sendKeysToElement(element, text) // Type with clear
getElementText(element)         // Get text with wait
isElementDisplayed(element)     // Check visibility
waitForElement(element)         // Explicit wait
```

### BaseTest.java
Base test class handling WebDriver lifecycle:

```java
@BeforeMethod setUp()      // Initialize WebDriver before each test
@AfterMethod tearDown()    // Close WebDriver after each test
navigateToBaseUrl()        // Navigate to configured base URL
getCurrentBrowser()        // Get current browser name
```

### Page Objects

#### LoginPage.java
```java
// Methods:
login(username, password)       // Complete login flow
enterUsername(username)
enterPassword(password)
clickLoginButton()
getErrorMessage()
isErrorMessageDisplayed()
isLoginSuccessful()
```

#### DashboardPage.java
```java
// Methods:
isDashboardLoaded()
getWelcomeMessage()
clickLogout()
getUserProfileText()
```

## Running Tests

### Run All Tests
```bash
cd automation_java
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=LoginTests
```

### Run Specific Test Method
```bash
mvn test -Dtest=LoginTests#testValidLogin
```

### Run with Specific Browser
```bash
# Firefox
mvn test -Dbrowser=firefox

# Edge
mvn test -Dbrowser=edge

# Create ChromeOptions if needed (handled by WebDriverManager)
mvn test -Dbrowser=chrome
```

### Run in Headless Mode
```bash
mvn test -Dheadless=true
```

### Run with Custom Config
```bash
# Update config.properties, then run
mvn test
```

### Generate Test Report
```bash
# Run tests and generate report
mvn test surefire-report:report

# Report location: target/site/surefire-report.html
```

## Test Cases

### LoginTests.java

#### Test 1: Valid Login
```
Scenario: User logs in with valid credentials
Given: User is on login page
When: User enters valid username and password and clicks login
Then: Dashboard page should load
And: Screenshot "valid_login_success" is captured
```

**Execution**:
```bash
mvn test -Dtest=LoginTests#testValidLogin
```

#### Test 2: Invalid Login
```
Scenario: User logs in with invalid credentials
Given: User is on login page
When: User enters invalid username and password and clicks login
Then: Error message should be displayed
And: Screenshot "invalid_login_error" is captured
```

**Execution**:
```bash
mvn test -Dtest=LoginTests#testInvalidLogin
```

## Logging

### Configure Logging
Log configuration: `src/main/resources/log4j2.xml`

**Output Locations**:
- Console: Real-time log output
- File: `logs/automation.log`

**Log Levels**:
- INFO: Important information
- DEBUG: Detailed debugging info
- ERROR: Error messages
- WARN: Warning messages

### Using Logger in Tests
```java
logger.info("Test started");
logger.debug("Debugging information");
logger.error("Error occurred", exception);
logger.warn("Warning message");
```

## Screenshots

### How Screenshots Work
- Screenshots are auto-captured on failure (not automatic by default)
- Manually capture: `screenshotUtil.takeScreenshot("name")`

**Location**: `screenshots/` directory

**Filename Format**: `name_yyyyMMdd_HHmmss.png`

### Capturing Screenshots in Tests
```java
// In test method
screenshotUtil.takeScreenshot("login_page_state");
screenshotUtil.takeScreenshot("error_message");
```

## WebDriverManager

WebDriverManager automatically downloads and manages browser drivers. No need to manually download chromedriver, geckodriver, etc.

**Features**:
- Automatic driver download
- Version detection
- Platform compatibility
- Cache management

## POM (Page Object Model) Pattern

### Creating New Page Objects

1. **Create page class** extending `BasePage`:
```java
package pages;

import base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MyPage extends BasePage {
    @FindBy(id = "element-id")
    private WebElement myElement;

    public MyPage(WebDriver driver) {
        super(driver);
    }

    public void clickMyElement() {
        clickElement(myElement);
    }
}
```

2. **Use in tests**:
```java
@Test
public void myTest() {
    MyPage page = new MyPage(driver);
    page.clickMyElement();
    // Assert results
}
```

## Adding New Tests

1. **Create test class** in `src/test/java/tests/`:
```java
package tests;

import base.BaseTest;
import pages.YourPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class YourTests extends BaseTest {
    @Test(description = "Test description")
    public void yourTestMethod() {
        YourPage page = new YourPage(driver);
        // Your test logic
        Assert.assertTrue(condition, "Assertion message");
    }
}
```

2. **Update testng.xml** if creating new test class:
```xml
<class name="tests.YourTests"/>
```

3. **Run test**:
```bash
mvn test -Dtest=YourTests
```

## Debugging

### Enable Debug Logging
Modify `log4j2.xml`:
```xml
<Root level="DEBUG">
```

### Debug in IDE
1. Set breakpoints in test code
2. Run in debug mode
3. Step through execution

### Quick Troubleshooting

| Issue | Solution |
|-------|----------|
| WebDriver not found | Ensure browser is installed |
| Timeout errors | Increase timeout values in config |
| Element not found | Verify CSS/XPath locators |
| Test fails unexpectedly | Check logs and screenshots |

## Integration with CI/CD

### GitHub Actions
```yaml
- name: Run Java Selenium Tests
  run: |
    cd automation_java
    mvn clean test
```

### Jenkins
```groovy
stage('Test') {
    steps {
        dir('automation_java') {
            sh 'mvn clean test'
        }
    }
}
```

## Best Practices

1. **Use PageFactory**: @FindBy annotations for cleaner code
2. **Explicit Waits**: Use wait methods instead of Thread.sleep()
3. **Meaningful Names**: Test names should describe what they test
4. **DRY Principle**: Keep common actions in BasePage
5. **Error Handling**: Catch and log exceptions appropriately
6. **Screenshots**: Capture at critical points
7. **Independent Tests**: Tests should not depend on each other
8. **Clean Code**: Follow Java naming conventions

## Troubleshooting

### ChromeDriver Issues
```
error: SessionNotCreatedException: session not created
- Ensure Chrome is installed
- Check WebDriverManager logs
- Verify browser compatibility
```

### Connection Refused
```
java.net.ConnectException: Connection refused
- Verify application is running
- Check base.url in config.properties
- Ensure network connectivity
```

### Frame/Window Handling
```
nosuchframeexception: no such frame
- Use switchTo() for frames
- Wait for frame to load before switching
```

### Element Not Found
```
nosuchelementexception: no such element
- Verify CSS selector/XPath
- Add explicit waits
- Check element visibility status
```

## Performance Tips

1. Run tests in parallel (TestNG feature)
2. Use headless mode for faster execution
3. Optimize waits
4. Minimize screenshots
5. Use appropriate assertion granularity

## Resources

- [Selenium Documentation](https://www.selenium.dev/documentation/)
- [TestNG Documentation](https://testng.org/doc/)
- [WebDriverManager GitHub](https://github.com/bonigarcia/webdrivermanager)
- [Log4j2 Documentation](https://logging.apache.org/log4j/2.x/)

## Support

For issues or questions:
1. Check logs in `logs/automation.log`
2. Review screenshots in `screenshots/`
3. Verify configuration in `config.properties`
4. Check test output for detailed errors

---

**Version**: 1.0
**Last Updated**: April 2026
