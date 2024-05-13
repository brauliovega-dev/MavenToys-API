package com.oreilly.maventoys.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Generic class for wrapping API responses, providing a unified structure that includes
 * a message and optionally any data returned by the API. This simplified response model
 * focuses on delivering the essential information to the client, making it easier to
 * handle responses without the need for HTTP status codes within the payload itself.
 *
 * @param <T> The type of the data field, allowing for flexibility in the type of data returned.
 */
@Getter
@Setter
public class CustomApiResponse<T> {

  /**
   * A message associated with the response, providing additional information or context.
   * This can include details on the outcome of the request, error messages, or any other
   * pertinent information.
   */
  private String message;

  /**
   * The data payload of the response. This field can contain any type of data appropriate to the specific API call,
   * allowing the response to be flexible and adaptable to different needs. The data can be null if there is no
   * relevant data to return for the request.
   */
  private T data;

  /**
   * Constructs a new ApiResponse with the specified message and data payload.
   * This constructor initializes the response object with the provided message and data,
   * creating a structured response for API consumers.
   *
   * @param newMessage A descriptive message associated with the response.
   * @param newData    The data payload of the response, which can be of any type relevant to the API call.
   */
  public CustomApiResponse(final String newMessage, final T newData) {
    this.message = newMessage;
    this.data = newData;
  }
}
