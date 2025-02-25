package com.abha.enms.configs.webclientconfig;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Configuration class for managing loop resource settings.
 * This class encapsulates various configuration parameters used to define
 * and control the behavior of loop resources, such as the number of threads,
 * transport type, and the name of the loop resource.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoopResourceConfig {
  private String name;
  private Integer threadCount;
  private Boolean useNativeTransport;
}
