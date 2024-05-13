/**
 * Provides classes for configuring Cross-Origin Resource Sharing (CORS) in a Spring Boot application.
 *
 * <p>This package contains configuration classes that define how CORS policies are applied across the application.
 * The primary configuration class, {@link com.oreilly.maventoys.config.CorsConfig}, implements
 * {@link org.springframework.web.servlet.config.annotation.WebMvcConfigurer}
 * to provide application-wide CORS settings. These settings include specifying allowed origins, HTTP methods,
 * headers, exposed headers, credential requirements, and the cache duration for preflight responses.</p>
 *
 * <p>The CORS configuration is crucial for web applications that need to interact with APIs hosted on a different
 * domain than the web application itself. Properly configuring CORS is essential for enabling web applications
 * to make cross-origin requests while maintaining security by restricting such requests as necessary.</p>
 *
 * <p>Usage of this package's configurations allows for fine-tuned control over CORS behavior, aiding in
 * balancing functionality and security for web applications. The configurations are externalized, meaning
 * allowed origins and other CORS policies can be defined in the application's properties files, allowing
 * for flexibility and ease of adjustments without requiring code changes.</p>
 *
 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurer
 */
package com.oreilly.maventoys.config;
