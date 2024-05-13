package com.oreilly.maventoys.exceptions;

import lombok.Getter;

/**
 * Represents a standardized structure for sending error details in API responses.
 * This class encapsulates information about errors that occur during API processing,
 * including HTTP status, error code, and a descriptive message.
 */
@Getter

public class ApiError {

  /**
   * A readable message providing more details about the error.
   */
  private final String message;

  /**
   * Constructs a new ApiError with the specified newStatus, newCode, and newMessage.
   *
   * @param newMessage A descriptive newMessage about the error.
   */
  public ApiError(final String newMessage) {
    this.message = newMessage;
  }
}
