package com.abha.enms.filters;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for registering servlet filters.
 * This class registers the {@link HeaderValidationFilter} to apply security or validation
 * checks on incoming API requests.
 */
@Configuration
public class FilterConfig {

  /**
   * Registers the {@link HeaderValidationFilter} to intercept requests to "/api/*" endpoints.
   *
   * @return a {@link FilterRegistrationBean} instance configuring the filter.
   */
  @Bean
  public FilterRegistrationBean<HeaderValidationFilter> headerValidationFilter() {
    FilterRegistrationBean<HeaderValidationFilter> registrationBean =
        new FilterRegistrationBean<>();

    registrationBean.setFilter(new HeaderValidationFilter());
    registrationBean.addUrlPatterns("/api/*");  // Apply filter to specific URL patterns
    registrationBean.setOrder(1);  // Set precedence if multiple filters

    return registrationBean;
  }
}
