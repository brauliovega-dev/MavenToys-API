package com.oreilly.maventoys.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * Entity representing an invoice generated for sale transactions within the application.
 * Invoices detail the products sold, quantities, pricing, and the overall financial transaction
 * between the business and its customers. Each invoice is linked to a specific sale and product,
 * reflecting the purchase details.
 */
@Entity
@Getter
@Setter
@Table(name = "invoices")
public class Invoice {

  /**
   * Unique identifier for the invoice. This ID is generated automatically
   * and used to uniquely identify an invoice within the database.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * The sale associated with this invoice. This many-to-one relationship
   * links the invoice to its corresponding sale record, detailing the transaction
   * that led to the invoice's creation.
   */
  @ManyToOne
  @JoinColumn(name = "sales_id")
  private Sale sale;

  /**
   * The product included in the sale. This many-to-one relationship links the invoice
   * to the specific product that was sold, allowing for detailed tracking of product sale.
   */
  @ManyToOne
  @JsonIgnore
  @JoinColumn(name = "product_id")
  private Product product;


  /**
   * Quantity of the product sold. This field details the number of units of the product
   * included in the transaction, essential for inventory and sale analysis.
   */
  @Column(name = "quantity")
  private Integer quantity;

  /**
   * Subtotal for the product sold, before any discounts are applied. This field represents
   * the gross financial value of the product portion of the transaction.
   */
  @Column(name = "subtotal")
  private double subtotal;

  /**
   * Discount applied to the product sale. This field captures any price reductions
   * offered at the time of sale, affecting the final transaction value.
   */
  @Column(name = "discount")
  private Integer discount;

  /**
   * Status of the invoice, indicating whether the invoice has been paid or remains outstanding.
   * This field is crucial for financial tracking and customer account management.
   */
  @Column(name = "status")
  private Boolean status;

  // Getters and setters...

}
