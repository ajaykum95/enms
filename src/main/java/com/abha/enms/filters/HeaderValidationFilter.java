package com.abha.enms.filters;

import com.abha.sharedlibrary.shared.constants.HeaderConstant;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * A servlet filter that validates the presence of required headers in incoming HTTP requests.
 * It ensures that specific headers, such as {@code USER_ID} and {@code CLIENT_ID}, are included
 * in the request. If a required header is missing or invalid, the request is rejected.
 */
public class HeaderValidationFilter implements Filter {

  /**
   * List of required headers that must be present in the request.
   */
  private static final List<String> REQUIRED_HEADERS = Arrays.asList(
      HeaderConstant.USER_ID, HeaderConstant.CLIENT_ID
  );

  /**
   * Filters incoming requests to check for required headers.
   *
   * @param servletRequest  the incoming request
   * @param servletResponse the response to be sent
   * @param filterChain     the filter chain to proceed with the request
   * @throws IOException      if an input or output error occurs
   * @throws ServletException if the request cannot be processed
   */
  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                       FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
    HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

    if (validHeader(httpRequest, httpResponse)) {
      return;
    }

    // Continue the filter chain if headers are valid
    filterChain.doFilter(servletRequest, servletResponse);
  }

  /**
   * Validates the presence and correctness of required headers in the request.
   * If a required header is missing, the response is set to HTTP 400 (Bad Request).
   * If the {@code CLIENT_ID} is invalid, the request is also rejected.
   *
   * @param httpRequest  the HTTP request
   * @param httpResponse the HTTP response
   * @return {@code true} if the request is invalid and should be blocked, {@code false} otherwise
   * @throws IOException if an error occurs while writing to the response
   */
  private static boolean validHeader(
      HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
    for (String header : REQUIRED_HEADERS) {
      String headerValue = httpRequest.getHeader(header);
      if (headerValue == null || headerValue.isEmpty()) {
        httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        httpResponse.getWriter().write("Missing required header: " + header);
        return true;
      }
      if (HeaderConstant.CLIENT_ID.equalsIgnoreCase(header)
          && !HeaderConstant.CLIENT_ALLOWED.contains(headerValue)) {
        httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        httpResponse.getWriter().write("Invalid client id: " + header);
        return true;
      }
    }
    return false;
  }
}