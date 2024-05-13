package com.oreilly.maventoys.model.DTO;

import lombok.Getter;
import lombok.Setter;


/**
 * Represents a Data Transfer Object (DTO) for an invoice. This DTO includes details related to a specific product's
 * sale, such as the product ID, quantity sold, applicable discounts, the subtotal for the product, and the sales
 * identifier. It's typically used to transfer data between processes or layers of the application, especially for
 * operations related to invoicing and sales processing.
 */
@Getter
@Setter
public class InvoicesDTO {

  /**
   * The ID of the product. This field is used to uniquely identify the product involved in the invoice.
   */
  private Integer product_id;

  /**
   * The quantity of the product being invoiced. This represents how many units of the product are included.
   */
  private Integer quantity;

  /**
   * The discount applied to the product, if any. This is usually a percentage value indicating how much
   * of a discount is applied to the product's price.
   */
  private Integer discount;

  /**
   * The subtotal for the product, calculated after applying the discount to the quantity of the product sold.
   * This value does not account for any taxes or additional fees.
   */
  private double subtotal;

  /**
   * An identifier for the sales transaction this invoice is part of. This allows for the invoice to be associated
   * with a specific sale.
   */
  private Integer sales;

  // Constructors, getters, and setters would be here
}
