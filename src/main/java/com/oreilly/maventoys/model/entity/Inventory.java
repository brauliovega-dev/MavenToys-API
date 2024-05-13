package com.oreilly.maventoys.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


/**
 * Entity representing the inventory record for products within the application.
 * Tracks the stock levels of each product to manage supply, fulfill orders, and
 * monitor inventory status. Each inventory record is linked to a specific product,
 * indicating the quantity of that product currently in stock.
 */
@Entity
@Getter
@Setter
@Table(name = "inventory")
public class Inventory {

  /**
   * Unique identifier for the inventory record. This ID is generated automatically
   * and used to uniquely identify an inventory entry within the database.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * The product associated with this inventory record. This many-to-one relationship
   * links the inventory record to its corresponding product, enabling tracking of
   * stock levels for individual products.
   */
  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  /**
   * The current stock level for the product. Indicates the quantity of the product
   * available in inventory. This field is essential for inventory management, order
   * fulfillment, and supply chain operations.
   */
  @Column(name = "stock_on_hand")
  private Integer stockOnHand;


  // Getters and setters

}
