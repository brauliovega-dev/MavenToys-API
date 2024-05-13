package com.oreilly.maventoys;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

  /**
   * Configures the Spring Boot application for deployment in a servlet server environment.
   * This method is called during the startup of the application in a servlet server context,
   * allowing for customization of the application's configuration through the {@link SpringApplicationBuilder}.
   *
   * @param application the Spring Boot application builder, used for configuring various facets of the application.
   *
   * @return the {@link SpringApplicationBuilder} configured with the application's sources, enabling its proper
   * deployment in a servlet server environment.
   *
   * @override Indicates that this method overrides the {@code configure} method from the parent class
   * {@link SpringBootServletInitializer}.
   */
  @Override
  protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
    return application.sources(MaventoysApplication.class);
  }


}
