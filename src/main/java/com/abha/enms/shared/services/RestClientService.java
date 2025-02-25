package com.abha.enms.shared.services;

import java.time.Duration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Service class for making RESTful HTTP requests using WebClient.
 * This class provides methods for sending HTTP POST and GET requests,
 * handling client errors, and managing response timeouts.
 */
@Component
public class RestClientService {

  private final WebClient webClient;

  /**
   * Constructs a new RestClientService with the provided WebClient instance.
   *
   * @param webClient The WebClient instance to use for making HTTP requests.
   */
  public RestClientService(WebClient webClient) {
    this.webClient = webClient;
  }
  /**
   * Sends an HTTP POST request to the specified URL with the provided
   * request body, headers, and response timeout.
   *
   * @param url The URL to which the POST request is sent.
   * @param bodyInserter The body inserter for the request body.
   * @param headers The HTTP headers to include in the request.
   * @param responseTimeoutMs The timeout duration for the response, in milliseconds.
   * @param typeResponse The type of the response expected from the server.
   * @param <Req> The type of the request body.
   * @param <Rsp> The type of the response body.
   * @return The response body of type {@code Rsp}.
   */
  public <Req, Rsp> Rsp post(
      final String url, BodyInserter<Req, ReactiveHttpOutputMessage> bodyInserter,
      final HttpHeaders headers, final int responseTimeoutMs,
      final ParameterizedTypeReference<Rsp> typeResponse) {
    return webClient.post().uri(url)
        .headers(httpHeaders -> httpHeaders.putAll(headers))
        .body(bodyInserter)
        .retrieve()
        .toEntity(typeResponse)
        .timeout(Duration.ofMillis(responseTimeoutMs))
        .block()
        .getBody();
  }

  /**
   * Sends an HTTP GET request to the specified URL with the provided headers and response timeout.
   *
   * @param url The URL to which the GET request is sent.
   * @param headers The HTTP headers to include in the request.
   * @param responseTimeoutMs The timeout duration for the response, in milliseconds.
   * @param typeResponse The type of the response expected from the server.
   * @param <Rsp> The type of the response body.
   * @return The response body of type {@code Rsp}.
   */
  public <Rsp> Rsp get(
      final String url, final HttpHeaders headers, final int responseTimeoutMs,
      final ParameterizedTypeReference<Rsp> typeResponse) {

    return webClient.get().uri(url)
        .headers(httpHeaders -> httpHeaders.putAll(headers))
        .retrieve()
        .toEntity(typeResponse)
        .timeout(Duration.ofMillis(responseTimeoutMs))
        .block()
        .getBody();
  }
}
