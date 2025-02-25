package com.abha.enms.configs.webclientconfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for managing web client properties.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "webclient")
public class WebClientPropertyConfig {
  private LoopResourceConfig loopResources;
  private ConnectionProviderConfig connectionProvider;
  private HttpClientConfig httpClient;
}
