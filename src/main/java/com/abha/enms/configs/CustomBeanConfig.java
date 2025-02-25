package com.abha.enms.configs;

import com.abha.enms.configs.webclientconfig.CustomLoggingHandler;
import com.abha.enms.configs.webclientconfig.WebClientPropertyConfig;
import io.netty.channel.ChannelOption;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.resources.LoopResources;

/**
 * Configuration class for custom beans.
 */
@Configuration
public class CustomBeanConfig {
  private final WebClientPropertyConfig webClientPropertyConfig;

  public CustomBeanConfig(WebClientPropertyConfig webClientPropertyConfig) {
    this.webClientPropertyConfig = webClientPropertyConfig;
  }

  /**
   * Manages web client related configurations.
   *
   * @return WebClient
   */
  @Bean
  public WebClient webClient(WebClient.Builder webClientBuilder) {
    // LoopResources for reactor-netty event loops
    LoopResources loopResources =
        LoopResources.create(webClientPropertyConfig.getLoopResources().getName(),
            webClientPropertyConfig.getLoopResources().getThreadCount(),
            webClientPropertyConfig.getLoopResources().getUseNativeTransport());
    // ConnectionProvider for managing connections and connection pool settings
    ConnectionProvider connectionProvider =
        ConnectionProvider.builder(webClientPropertyConfig.getConnectionProvider().getName())
            .maxConnections(webClientPropertyConfig.getConnectionProvider().getMaxConnections())
            .pendingAcquireMaxCount(
                webClientPropertyConfig.getConnectionProvider().getPendingAcquireMaxCount())
            .pendingAcquireTimeout(Duration.ofMillis(
                webClientPropertyConfig.getConnectionProvider().getPendingAcquireTimeout()))
            .maxIdleTime(Duration.ofSeconds(
                webClientPropertyConfig.getConnectionProvider().getMaxIdleTime()))
            .maxLifeTime(Duration.ofSeconds(
                webClientPropertyConfig.getConnectionProvider().getMaxLifeTime()))
            .build();

    // HttpClient creation and configuration
    HttpClient httpClient = HttpClient.create(connectionProvider)
        .doOnConnected(connection -> connection.addHandlerFirst(new CustomLoggingHandler()))
        .compress(webClientPropertyConfig.getHttpClient().getCompress())
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,
            webClientPropertyConfig.getHttpClient().getConnectTimeoutMillis())
        .option(ChannelOption.TCP_NODELAY, webClientPropertyConfig.getHttpClient().getTcpNoDelay())
        .option(ChannelOption.SO_KEEPALIVE, webClientPropertyConfig.getHttpClient().getKeepAlive())
        .option(ChannelOption.SO_REUSEADDR,
            webClientPropertyConfig.getHttpClient().getReuseAddress())
        .runOn(loopResources);

    // ReactorClientHttpConnector creation with the configured HttpClient
    ReactorClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);

    // WebClient creation and configuration
    return webClientBuilder
        .clientConnector(connector)
        .codecs(config -> config.defaultCodecs()
            .maxInMemorySize(webClientPropertyConfig.getHttpClient().getMaxInMemorySize()))
        .build();
  }
}