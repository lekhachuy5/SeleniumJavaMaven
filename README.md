# SeleniumJavaMaven

A comprehensive Selenium WebDriver test automation framework built with Java, Maven, and Cucumber BDD. This project provides a robust foundation for creating, managing, and executing automated web tests with support for multiple browsers and testing frameworks.

## Features

- **Multi-Browser Support**: Run tests on Chrome, Firefox, and Edge browsers
- **BDD Framework**: Integration with Cucumber for behavior-driven development
- **Page Object Model**: Organized and maintainable test structure
- **Excel Data Management**: Support for data-driven testing using Apache POI
- **Database Testing**: SQL Server database verification utilities
- **Logging**: Comprehensive logging using Log4j and SLF4J
- **Screenshot Capabilities**: Automatic screenshot capture using AShot
- **WebDriver Management**: Automatic driver management with WebDriverManager
- **Multiple Test Frameworks**: Support for both JUnit and TestNG
- **Performance Testing**: JMeter integration for performance testing capabilities

## Prerequisites

Before running this project, ensure you have the following installed:

- **Java JDK 14** or higher
- **Apache Maven 3.6+**
- **Git** (for version control)
- Web browsers (Chrome, Firefox, or Edge) depending on your testing needs

## Installation

1. Clone the repository:
```bash
git clone https://github.com/lekhachuy5/SeleniumJavaMaven.git
cd SeleniumJavaMaven
```

2. Install dependencies:
```bash
mvn clean install
```

3. Configure the project:
   - Update `src/config.properties` with your environment-specific settings
   - Configure browser preferences in the BaseSetup class if needed

## Project Structure

```
SeleniumJavaMaven/
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── base/           # Base setup and browser configuration
│   │       ├── common/         # Common utilities and context management
│   │       └── utils/          # Utility classes (Excel, DB, File, etc.)
│   └── test/
│       ├── java/
│       │   ├── runner/         # Test runners
│       │   └── stepdefinitions/ # Cucumber step definitions
│       └── resources/
│           └── features/       # Cucumber feature files
├── pom.xml                     # Maven configuration
└── README.md                   # This file
```

## Technologies Used

- **Selenium WebDriver 4.9.0** - Browser automation
- **Cucumber 7.12.1** - BDD framework
- **Maven** - Build and dependency management
- **JUnit 4.13.2** - Unit testing framework
- **TestNG 7.8.0** - Testing framework
- **Apache POI 5.2.3** - Excel file handling
- **Log4j 2.17.2** - Logging framework
- **WebDriverManager 5.3.3** - Automatic driver management
- **AShot 1.5.4** - Screenshot utilities
- **SQL Server JDBC 11.2.3** - Database connectivity
- **JMeter 5.6** - Performance testing

## Usage

### Running Tests

Execute all tests:
```bash
mvn test
```

Run specific feature files:
```bash
mvn test -Dcucumber.options="src/test/resources/features/demo/demo_test.feature"
```

Run tests with specific tags:
```bash
mvn test -Dcucumber.options="--tags @DemoFeatureFile"
```

### Writing Tests

1. Create a feature file in `src/test/resources/features/`:
```gherkin
Feature: Login Test
  
  Scenario: Successful login
    Given scenario executes TC1 to verify for journey
    When Enter the Username and Password
    Then Verify user is logged in
```

2. Implement step definitions in `src/test/java/stepdefinitions/`

3. Run your tests using the Runner class or Maven commands

## Configuration

The project uses a `config.properties` file for environment-specific configurations. Update this file with:
- Application URLs
- Browser preferences
- Timeout settings
- Database connection strings
- Other environment variables

## Browser Configuration

The framework supports multiple browsers through the BaseSetup class:
- Chrome (default)
- Firefox
- Edge

Browser selection can be configured in the properties file or programmatically.

## Contributing

Contributions are welcome! To contribute:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/YourFeature`)
3. Commit your changes (`git commit -m 'Add some feature'`)
4. Push to the branch (`git push origin feature/YourFeature`)
5. Open a Pull Request

## Best Practices

- Follow the Page Object Model pattern for better maintainability
- Keep step definitions simple and reusable
- Use meaningful names for scenarios and steps
- Implement proper wait strategies (avoid Thread.sleep())
- Add logging for better debugging
- Keep test data separate from test logic

## Troubleshooting

**WebDriver Issues:**
- The project uses WebDriverManager to automatically download and manage browser drivers
- Ensure your browser version is compatible with Selenium 4.9.0

**Build Issues:**
- Run `mvn clean install` to resolve dependency issues
- Ensure Java 14 or higher is installed and configured

**Test Execution Issues:**
- Check the logs in the console output
- Verify that the config.properties file is properly configured
- Ensure required browsers are installed

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Author

- **lekhachuy5**

## Acknowledgments

- Selenium WebDriver community
- Cucumber BDD framework
- Apache Maven project
- All contributors and users of this framework
