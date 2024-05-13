package com.oreilly.maventoys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class MaventoysApplication extends SpringBootServletInitializer {
  /**
   * The main entry point for the Spring Boot application that serves as an API for managing a business.
   * <p>
   * This application provides a backend interface for interacting with a SQL database, offering CRUD (Create, Read,
   * Update, Delete) functionalities across different sections such as employees, products, sales, stores, and
   * categories. Upon starting, the application automatically configures database connections and sets up the
   * necessary API endpoints for operations in each section.
   * </p>
   * <p>
   * This method utilizes {@link SpringApplication#run} to boot the application, which includes automatic Spring Boot
   * configuration, component scanning, and application context initialization. Command-line arguments can be used to
   * modify startup configurations, such as specifying a different configuration profile or adjusting specific
   * application properties.
   * </p>
   *
   * @param args Command line arguments passed to the application, used for configuring specific aspects of the
   *             application and its execution.
   *
   * @see SpringApplication#run(Class, String...) for more details on the application bootstrapping process.
   */
  public static void main(final String[] args) {
    SpringApplication.run(MaventoysApplication.class, args);
  }

}
