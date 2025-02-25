package com.abha.enms.configs.webclientconfig;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Configuration class for managing connection provider settings.
 * This class encapsulates various configuration parameters used to define
 * and control the behavior of a connection provider, such as the maximum
 * number of connections, timeouts, and lifecycle settings.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConnectionProviderConfig {
  private String name;
  private Integer maxConnections;
  private Integer pendingAcquireMaxCount;
  private Integer pendingAcquireTimeout;
  private Integer maxIdleTime;
  private Integer maxLifeTime;
}
