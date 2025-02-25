package com.abha.enms.exceptions;

import com.abha.sharedlibrary.exceptions.AbhaBaseRunTimeException;
import com.abha.sharedlibrary.exceptions.BaseResponseError;
import com.abha.sharedlibrary.exceptions.ErrorCategory;
import com.abha.sharedlibrary.exceptions.ExceptionTypes;
import java.util.Collections;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for handling exceptions across the whole application.
 *
 * <p>This class provides methods to handle various exceptions and return appropriate
 * responses with HTTP status codes and error messages.
 */
@ControllerAdvice
public final class GlobalExceptionHandler {

  /**
   * Handles general exceptions and returns a standardized response.
   *
   * @param ex the exception to handle
   * @return a {@code ResponseEntity} containing a {@code BaseResponse} with error details
   *     and HTTP status
   */
  @ExceptionHandler
  public ResponseEntity<?> handleException(Exception ex) {
    var httpStatusCode = getHttpStatusCode(ex);
    List<BaseResponseError> errors = buildErrors(ex);
    return new ResponseEntity<>(errors, httpStatusCode);
  }

  private List<BaseResponseError> buildErrors(Exception ex) {
    if (ex instanceof AbhaBaseRunTimeException aex) {
      return aex.getExceptionList();
    } else if (ex instanceof HttpMessageNotReadableException hmnRdEx) {
      return Collections.singletonList(
          BaseResponseError.builder().businessErrorCode(10000).errorMessage(ex.getMessage())
              .errorCategory(ErrorCategory.ERROR).exType(ExceptionTypes.BAD_REQUEST_ERROR)
              // .errorStack("null") // TODO: Have to set this properly ...
              .build());
    }
    // call default error builder
    return buildDefaultErrors(ex);
  }

  private List<BaseResponseError> buildDefaultErrors(Exception ex) {
    return Collections.singletonList(
        BaseResponseError.builder().businessErrorCode(0).errorMessage(ex.getMessage())
            .errorCategory(ErrorCategory.ERROR).exType(ExceptionTypes.INTERNAL_ERROR)
            // .errorStack("null") // TODO: Have to set this properly ...
            .build());
  }

  private HttpStatus getHttpStatusCode(Exception ex) {
    if (ex instanceof AbhaBaseRunTimeException aex) {
      return getHttpStatusCode(aex);
    } else if (ex instanceof HttpMessageNotReadableException hmnRdEx) {
      return HttpStatus.BAD_REQUEST;
    }
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }

  private HttpStatus getHttpStatusCode(AbhaBaseRunTimeException aex) {
    return switch (aex.getExceptionList().get(0).exType()) {
      case VALIDATION_ERROR, BAD_REQUEST_ERROR -> HttpStatus.BAD_REQUEST;
      case UNAUTHORIZED -> HttpStatus.UNAUTHORIZED;
      default -> HttpStatus.INTERNAL_SERVER_ERROR;
    };
  }

}