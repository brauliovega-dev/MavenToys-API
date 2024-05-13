package com.oreilly.maventoys.exceptions;


import com.oreilly.maventoys.model.CustomApiResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Provides global exception handling across all controller classes. It captures exceptions and formats them into
 * a consistent response structure. This class simplifies error handling and enhances API responses with meaningful
 * HTTP status codes and error messages.
 */
@ControllerAdvice
@Getter
public class GlobalExceptionHandler {

  /**
   * Handles not found entities by returning a standardized error response.
   *
   * @param ex The caught EntityNotFoundException.
   *
   * @return A {@link ResponseEntity} encapsulating an {@link ApiError} with NOT_FOUND status.
   */
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<Object> handleEntityNotFoundException(final EntityNotFoundException ex) {
    ApiError apiError = new ApiError(ex.getMessage());
    return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
  }



  /**
   * Handles custom IdNotFound exceptions, indicating a requested entity was not found.
   *
   * @param notFound The caught IdNotFound exception.
   *
   * @return A {@link ResponseEntity} containing an {@link CustomApiResponse} with a BAD_REQUEST status and the
   * error details.
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(IdNotFound.class)
  public ResponseEntity<CustomApiResponse<ApiError>> idNotFound(final IdNotFound notFound) {
    ApiError apiError = new ApiError(notFound.getMessage());
    CustomApiResponse<ApiError> customApiResponse = new CustomApiResponse<>("Entity not found", apiError);
    return new ResponseEntity<>(customApiResponse, HttpStatus.NOT_FOUND);
  }
}

