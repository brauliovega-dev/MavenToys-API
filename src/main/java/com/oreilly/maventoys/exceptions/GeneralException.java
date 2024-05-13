package com.oreilly.maventoys.exceptions;

/**
 * Custom exception class that extends {@link RuntimeException}. It is designed to represent
 * general errors that occur within the application's runtime environment. This class can be
 * used to encapsulate a variety of non-specific exceptions that do not fit more narrowly defined
 * exception categories.
 */
public class GeneralException extends RuntimeException {

  /**
   * Constructs a new GeneralException with the specified detail message.
   * The message can be retrieved later by the {@link Throwable#getMessage()} method.
   *
   * @param message the detail message. The detail message is saved for later retrieval by the
   *                {@link Throwable#getMessage()} method.
   */
  public GeneralException(final String message) {
    super(message);
  }

  /**
   * Constructs a new GeneralException with the specified detail message and cause.
   * Note that the detail message associated with {@code cause} is not automatically incorporated
   * into this exception's detail message.
   *
   * @param message the detail message. The detail message is saved for later retrieval by the
   *                {@link Throwable#getMessage()} method.
   * @param cause   the cause (which is saved for later retrieval by the {@link Throwable#getCause()} method).
   *                (A {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.)
   */
  public GeneralException(final String message, final Throwable cause) {
    super(message, cause);
  }
}


