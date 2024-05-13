package com.oreilly.maventoys.model.DTO;

import lombok.Getter;


/**
 * Represents a response containing stock information.
 */
@Getter
public class StockResponse {
  /**
   * The stock quantity.
   */
  private Integer stock;

  /**
   * Constructs a new StockResponse object with the specified newStock.
   *
   * @param newStock The newStock information to be included in the response.
   */
  public StockResponse(final Integer newStock) {
    this.stock = newStock;
  }
}

