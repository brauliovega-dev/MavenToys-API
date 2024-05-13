package com.oreilly.maventoys.exceptions;

/**
 * Custom exception class that extends {@link RuntimeException}. It is specifically designed
 * to signify situations where an entity with a specified identifier cannot be found within
 * the application's persistence layer. This exception is typically thrown in response to
 * lookup operations for non-existent identifiers.
 */
public class IdNotFound extends RuntimeException {
  /**
   * Constructs a new IdNotFound exception with the specified detail message. This message
   * is intended to provide additional information about the error, such as the missing entity's
   * identifier or the context in which the error occurred.
   *
   * @param message the detail message. The detail message is saved for later retrieval by the
   *                {@link Throwable#getMessage()} method.
   */
  public IdNotFound(final String message) {
    super(message);
  }
}

