package com.oreilly.maventoys.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for setting up CORS in a Spring Boot application.
 * This class provides a basic CORS configuration.
 *
 * <p>This class can be extended to modify CORS configurations. When extending,
 * ensure that the CORS policy requirements of the application are met.
 * Use the {@code addCorsMappings} method to customize the CORS settings.
 * Note that changes in CORS settings can have significant security
 * implications.</p>
 *
 * @see WebMvcConfigurer
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
  /**
   * Array of allowed origins. Values are injected from the application
   * properties file.
   */
  @Value("${cors.allowedOrigins}")
  private String[] allowedOrigins;

  /**
   * The maximum age of the cache for preflight responses in seconds.
   * Default value is set to 3600 seconds (1 hour). Adjust as necessary.
   */
  private static final int MAX_AGE = 3600;

  /**
   * Configures CORS mapping for the application.
   * This method can be overridden to customize CORS settings.
   * Ensure that any overridden implementation adheres to the application's
   * security requirements.
   * <p>
   * CORS configuration details:
   * - The allowed origins are externalized to the application properties file.
   * - Allowed HTTP methods are GET, POST, PUT, DELETE, PATCH, OPTIONS.
   * - All headers are allowed.
   * - Exposed headers include "x-auth-token" and "x-app-id".
   * - Credentials configuration can be changed to true if necessary.
   * - Cache lifetime for preflight responses is specified by MAX_AGE (in
   * seconds).
   *
   * @param registry the CORS registry to configure
   */
  @Override
  public void addCorsMappings(final CorsRegistry registry) {
    registry.addMapping("/**").allowedOrigins(allowedOrigins)
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS").allowedHeaders("*")
            .exposedHeaders("x-auth-token", "x-app-id").allowCredentials(true).maxAge(MAX_AGE);
  }
}
