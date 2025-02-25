package com.abha.enms.configs.webclientconfig;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Configuration class for HTTP client settings.
 * Provides parameters to control client behavior such as timeouts,
 * compression, and network options.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HttpClientConfig {
  private Boolean compress;
  private Integer connectTimeoutMillis;
  private Boolean tcpNoDelay;
  private Boolean keepAlive;
  private Boolean reuseAddress;
  private Integer maxInMemorySize;
}
