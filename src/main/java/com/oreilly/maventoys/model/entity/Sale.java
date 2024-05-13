package com.oreilly.maventoys.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * Entity representing a sales transaction within the application. Sale encapsulate the
 * details of transactions conducted, including total amount, associated store, employee
 * who handled the sale, and the specific invoices generated as a result of the sale.
 */
@Entity
@Getter
@Setter
@Table(name = "sales")
public class Sale {

  /**
   * Unique identifier for the sales transaction. This ID is automatically generated
   * and used to uniquely identify a sales transaction within the database.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * The store where the sales transaction took place. This many-to-one relationship
   * indicates the physical or logical location associated with the sale, facilitating
   * tracking of sales by location.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "store_id")
  private Store store;

  /**
   * Total amount of the sales transaction. This field captures the financial volume
   * of the transaction, including all items sold in the transaction.
   */
  @Column(name = "total")
  private double total;

  /**
   * Date when the sales transaction was conducted. This field is crucial for financial
   * reporting, trend analysis, and operational planning.
   */
  @Column(name = "date", columnDefinition = "TIMESTAMP")
  private LocalDate date;

  /**
   * Date when the sales transaction was conducted. This field is crucial for financial
   * reporting, trend analysis, and operational planning.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "employee_id")
  private Employee employee;

  /**
   * Invoices generated from the sales transaction. This one-to-many relationship links
   * the sale to its detailed invoices, each representing a part of the transaction or
   * specific items sold. Marked with @JsonIgnore to prevent serialization issues.
   */
  @JsonIgnore
  @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
  private List<Invoice> invoices;

}
