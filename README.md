# Template Project for Spring Boot

## Description
This project serves as a template for Spring Boot applications, incorporating various dependencies for comprehensive
functionality, including web development, data access, validation, and security among others. It is configured with
Maven for dependency management and build automation.

## Getting Started

### Prerequisites

- **Java 17**
- **Maven 3.8.1** or higher

## Features

- **Spring Boot Starters**: Web, Data JPA, Validation, Thymeleaf for rapid development.
- **Database Connectivity**: MySQL Connector for database operations.
- **API Documentation**: SpringDoc OpenAPI for API documentation.
- **Security**: Azure Entra ID (Active Directory) for secure authentication.
- **Code Simplification**: Lombok and MapStruct for reducing boilerplate code.
- **Testing**: JUnit and Spring Boot Starter Test for unit and integration testing.
- **Development Tools**: Spring Boot DevTools for automatic restarts and live reload.
- **Code Quality**: CheckStyle for enforcing coding standards.

## Dependencies

- Spring Boot (Data JPA, Validation, Web, Thymeleaf)
- MySQL Connector-J
- SpringDoc OpenAPI
- Azure Entra ID (Active Directory)
- OAuth2 Resource Server
- JSON Web Token (JJWT)
- Lombok and Lombok-Mapstruct Binding
- MapStruct
- Jakarta Persistence API
- JUnit (for testing)
- Spring Boot DevTools (optional for runtime)

## Building and Deployment

The project is packaged as a WAR file, suitable for deployment to any standard Java servlet container.

## Configuring CheckStyle in IntelliJ IDEA

CheckStyle is a development tool to help programmers write Java code that adheres to a coding standard. It automates the
process of checking Java code. To integrate CheckStyle with IntelliJ IDEA and ensure your code meets the coding
standards defined in your project, follow the steps below:

### Step 1: Install the CheckStyle-IDEA Plugin

- Open IntelliJ IDEA.
- Go to **File** > **Settings** (on macOS, **IntelliJ IDEA** > **Preferences**).
- Navigate to **Plugins**.
- Click on the **Marketplace** tab and search for **CheckStyle-IDEA**.
- Click **Install** and then **Restart** IntelliJ IDEA if prompted.

### Step 2: Configure the CheckStyle Plugin

- After restarting, open **Settings/Preferences** again.
- Navigate to **Tools** > **Checkstyle**.
- In the **Configuration File** section, click on the **+** sign to add a new configuration.
- Provide a description for your configuration (e.g., "O'Reilly Auto Parts Checkstyle").
- Click on the **Browse** button to select the `checkstyle.xml` file located in your project.
- Click **Next** and then **Finish**.

### Step 3: Running CheckStyle

To run CheckStyle on your project, right-click on any directory or file in your project structure, then select **Analyze
** > **Inspect Code** with your CheckStyle configuration. You can also use the CheckStyle tool window at the bottom of
the IDE to run checks and view violations.

### Step 4: Autocorrecting CheckStyle Violations

While CheckStyle-IDEA does not automatically fix all types of violations, IntelliJ IDEA's built-in inspections and
quick-fixes can help you address many common issues. Place your cursor on the highlighted error and press `Alt+Enter` to
see suggested fixes.