# NBA Automation Framework

A comprehensive test automation framework for NBA website testing, designed to automate test cases for three NBA team websites: Warriors (Core Product), Sixers (Derived Product 1), and Bulls (Derived Product 2).

## Framework Architecture

This framework follows a **Multi-Module Maven Project** structure with the following components:

```
nba-automation-framework/
├── automation-framework/          # Core framework components
├── core-product-tests/            # Warriors website tests
├── derived-product1-tests/        # Sixers website tests
├── derived-product2-tests/        # Bulls website tests
└── testng-complete-suite.xml     # Complete test suite
```

## Technology Stack

- **Java 11**
- **Selenium WebDriver 4.15.0**
- **TestNG 7.8.0**
- **Cucumber 7.14.0**
- **Spring Framework 5.3.23**
- **Maven 3.8+**
- **Log4j2 2.20.0**
- **ExtentReports 5.1.1**
- **Jackson (JSON/YAML processing)**
- **WebDriverManager 5.5.3**

## Framework Features

### ✅ Must-Have Features (Mandatory)
- ✅ Maven project with `src/main` and `src/test` structure
- ✅ Feature Files, Step Definitions, Business Logic, Page Objects, Test Data separation
- ✅ Parameterized configuration (no hard-coded values)
- ✅ Limited static waits usage
- ✅ Page Object Model design pattern
- ✅ Cucumber Runner classes with appropriate options
- ✅ Parallel test execution via TestNG XML
- ✅ Comprehensive reporting

### ✅ Good-to-Have Features (Bonus)
- ✅ Multi-Module Maven project with 4 modules
- ✅ Shared automation framework module
- ✅ Dynamic browser binary usage
- ✅ Zero usage of `By` class in Page Objects
- ✅ Single `PageFactory.initElements()` call
- ✅ JSON/YAML test data support
- ✅ Effective Cucumber Hooks usage
- ✅ Runtime Cucumber tag binding
- ✅ Multi-threaded execution via Maven commands

## Test Cases Implemented

### Test Case 1: Warriors (Core Product) - Shop Menu Jackets
- **Objective**: Find all Jackets from Shop Menu → Men's section
- **Actions**: 
  - Navigate to Warriors homepage
  - Go to Shop Menu → Men's
  - Find all Jackets from paginated pages
  - Store Jacket Price, Title, and Top Seller message to text file
  - Attach text file to report

### Test Case 2: Warriors (Core Product) - New & Features Videos
- **Objective**: Count video feeds in New & Features section
- **Actions**:
  - Navigate to Warriors homepage
  - Hover on menu item → click New & Features
  - Count total number of Video Feeds
  - Count videos present for ≥3 days
  - Validate video counts

### Test Case 3: Sixers (Derived Product 1) - Tickets Slides
- **Objective**: Count and validate slides below Tickets Menu
- **Actions**:
  - Navigate to Sixers homepage
  - Scroll to Tickets section
  - Count number of slides
  - Get title of each slide and validate with expected data
  - Count duration of each slide and validate with expected duration

### Test Case 4: Bulls (Derived Product 2) - Footer Links
- **Objective**: Find and validate footer links
- **Actions**:
  - Navigate to Bulls homepage
  - Scroll down to footer
  - Find all hyperlinks in footer
  - Store footer links to CSV file
  - Check for duplicate hyperlinks and report findings

## Project Structure

```
nba-automation-framework/
├── automation-framework/
│   ├── src/main/java/com/nba/framework/
│   │   ├── driver/           # WebDriver management
│   │   ├── pages/           # Base page class
│   │   ├── assertions/      # Base assertion class
│   │   ├── utils/          # Utility classes
│   │   ├── config/         # Configuration management
│   │   └── hooks/          # Cucumber hooks
│   └── src/main/resources/
│       ├── config.properties
│       └── log4j2.xml
├── core-product-tests/
│   ├── src/main/java/com/nba/tests/core/
│   │   ├── pages/          # Warriors page objects
│   │   ├── assertions/    # Warriors assertions
│   │   └── stepdefinitions/ # Warriors step definitions
│   └── src/test/resources/
│       └── features/      # Warriors feature files
├── derived-product1-tests/
│   ├── src/main/java/com/nba/tests/sixers/
│   │   ├── pages/         # Sixers page objects
│   │   ├── assertions/    # Sixers assertions
│   │   └── stepdefinitions/ # Sixers step definitions
│   └── src/test/resources/
│       └── features/      # Sixers feature files
└── derived-product2-tests/
    ├── src/main/java/com/nba/tests/bulls/
    │   ├── pages/         # Bulls page objects
    │   ├── assertions/    # Bulls assertions
    │   └── stepdefinitions/ # Bulls step definitions
    └── src/test/resources/
        └── features/      # Bulls feature files
```

## Setup Instructions

### Prerequisites
- Java 11 or higher
- Maven 3.8 or higher
- Chrome/Firefox/Edge browser
- Internet connection

### Installation

1. **Clone/Download the project**
   ```bash
   cd /path/to/your/workspace
   ```

2. **Navigate to project directory**
   ```bash
   cd nba-automation-framework
   ```

3. **Install dependencies**
   ```bash
   mvn clean install
   ```

### Configuration

Edit `automation-framework/src/main/resources/config.properties`:

```properties
# Browser Configuration
browser=chrome
browser.version=latest

# Execution Configuration
remote.execution=false
remote.hub.url=http://localhost:4444/wd/hub

# NBA URLs
nba.warriors.url=https://www.nba.com/warriors
nba.sixers.url=https://www.nba.com/sixers/
nba.bulls.url=https://www.nba.com/bulls/
```

## Running Tests

### 1. Run All Tests
```bash
mvn test -DsuiteXmlFile=testng-complete-suite.xml
```

### 2. Run Individual Modules

**Warriors Tests (Core Product):**
```bash
cd core-product-tests
mvn test
```

**Sixers Tests (Derived Product 1):**
```bash
cd derived-product1-tests
mvn test
```

**Bulls Tests (Derived Product 2):**
```bash
cd derived-product2-tests
mvn test
```

### 3. Run with Different Browsers
```bash
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge
```

### 4. Run Specific Test Cases
```bash
mvn test -Dcucumber.filter.tags="@TC1"
mvn test -Dcucumber.filter.tags="@TC2"
mvn test -Dcucumber.filter.tags="@TC3"
mvn test -Dcucumber.filter.tags="@TC4"
```

### 5. Run in Parallel
```bash
mvn test -Dparallel=true -DthreadCount=3
```

## Test Reports

### 1. Cucumber Reports
- **HTML Report**: `target/cucumber-reports/index.html`
- **JSON Report**: `target/cucumber-reports/Cucumber.json`
- **JUnit Report**: `target/cucumber-reports/Cucumber.xml`

### 2. ExtentReports
- **Location**: `test-output/reports/`
- **Format**: HTML with screenshots and detailed logs

### 3. Screenshots
- **Location**: `test-output/screenshots/`
- **Format**: PNG files with timestamps

### 4. Test Data Output
- **Jacket Details**: `test-output/jacket_details_*.txt`
- **Footer Links**: `test-output/footer_links_*.csv`

## Framework Components

### 1. Driver Management
- **DriverManager**: Handles WebDriver initialization and cleanup
- **Multi-browser support**: Chrome, Firefox, Edge
- **Remote execution**: Selenium Grid support
- **Thread-safe**: ThreadLocal implementation

### 2. Page Object Model
- **BasePage**: Common page operations and utilities
- **Page Objects**: Specific page implementations
- **Locator management**: Centralized locator definitions
- **Action methods**: Reusable page interactions

### 3. Assertions
- **BaseAssertion**: Custom assertion methods
- **Soft assertions**: Non-failing assertion support
- **Detailed logging**: Assertion result logging

### 4. Configuration Management
- **ConfigManager**: Centralized configuration
- **Properties file**: External configuration
- **Environment-specific**: Different configs for different environments

### 5. Utilities
- **FileUtils**: File operations (read/write CSV, text files)
- **ScreenshotUtils**: Screenshot capture and management
- **Logging**: Comprehensive logging with Log4j2

## Best Practices Implemented

1. **Separation of Concerns**: Clear separation between pages, assertions, and step definitions
2. **Reusability**: Shared framework components across all modules
3. **Maintainability**: Centralized configuration and utilities
4. **Scalability**: Multi-module structure for easy expansion
5. **Reporting**: Comprehensive reporting with screenshots and logs
6. **Error Handling**: Robust error handling and logging
7. **Parallel Execution**: Support for parallel test execution
8. **Cross-browser Testing**: Support for multiple browsers

## Troubleshooting

### Common Issues

1. **WebDriver Issues**
   - Ensure browser is installed
   - Check WebDriverManager configuration
   - Verify browser version compatibility

2. **Test Failures**
   - Check network connectivity
   - Verify website URLs are accessible
   - Review logs in `test-output/logs/`

3. **Configuration Issues**
   - Verify `config.properties` settings
   - Check Maven dependencies
   - Ensure Java version compatibility

### Logs
- **Location**: `test-output/logs/automation.log`
- **Level**: DEBUG for framework, INFO for tests
- **Rotation**: Daily rotation with size limits

## Contributing

1. Follow the existing code structure
2. Add proper logging to new methods
3. Update documentation for new features
4. Ensure all tests pass before committing

## License

This project is created for assessment purposes and follows the requirements specified in the assignment.

---

**Framework Architecture Diagram**: The framework follows a layered architecture with clear separation between framework components, page objects, test logic, and configuration management, ensuring maintainability and scalability.
