package com.oreilly.maventoys.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate; //java.sql.date // sales timestamp
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a store within the application. Stores are physical or conceptual
 * locations where sales transactions occur. This entity captures essential details about
 * each store, including its name, location, and operational status, and associates it with
 * sales transactions to facilitate tracking and management of business activities.
 */
@Entity
@Getter
@Setter
@Table(name = "stores")
public class Store {


  /**
   * The maximum length allowed for the name.
   * This constant defines the upper limit for the number of characters
   * the name string can contain. It is used to validate input and ensure
   * that names do not exceed this length for data integrity and consistency.
   */
  private static final int MAX_NAME_LENGTH = 100;

  /**
   * Unique identifier for the store. This ID is generated automatically
   * and is used to uniquely identify a store within the database.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * Sale transactions associated with this store. This one-to-many relationship
   * links the store to its sales transactions, allowing for analysis and reporting
   * of sales activity by store.
   */
  @OneToMany(mappedBy = "store")
  private List<Sale> sales = new ArrayList<>();

  /**
   * Name of the store, subject to validation constraints. The name is required and
   * must be between 1 and 100 characters long, ensuring it is meaningful and unique
   * but not excessively long.
   */
  @NotBlank(message = "Name is required")
  @Size(min = 1, max = MAX_NAME_LENGTH, message = "Name must be between 1 and 100 characters")
  private String name;
  /**
   * City where the store is located. This field is required and helps categorize stores
   * by geographic location, facilitating regional management and marketing strategies.
   */
  @NotBlank(message = "City is required")
  private String city;

  /**
   * Specific location of the store, providing more detailed information than the city alone.
   * This field is required and can be used for directions, local advertising, and logistical planning.
   */
  @NotBlank(message = "Location is required")
  private String location;

  /**
   * Date when the store was opened. This information is useful for tracking the age of the store,
   * planning anniversaries, and analyzing sales trends over time.
   */
  @Column(name = "open_date")
  private LocalDate openDate;

  /**
   * Flag indicating whether the store is currently active. Active stores are operational and
   * contributing to the company's sales and revenue, while inactive stores may be temporarily
   * closed or permanently shut down.
   */
  private Boolean active;

  // Getters y setters...

}
