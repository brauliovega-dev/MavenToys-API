package com.oreilly.maventoys.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Represents a category for organizing products into hierarchical groups in the application.
 * Facilitates product management and browsing by categorizing products. Each category is
 * linked to multiple products through a one-to-many relationship, indicating its contents.
 * Categories can be marked as active or inactive, controlling their visibility and availability
 * for product association.
 */
@Entity
@Getter
@Setter
@Table(name = "categories")
public class Category {

  /**
   * Unique identifier for the category. This ID is generated automatically
   * and used to uniquely identify a category within the database.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * Name of the category. This is a human-readable label used to identify and
   * describe the category within the application's user interface and reports.
   */
  private String name;

  /**
   * Flag indicating whether the category is active. Active categories are available
   * for use within the application, while inactive categories are hidden or archived.
   */
  private Boolean active;

  /**
   * List of products associated with the category. This establishes a one-to-many
   * relationship between a category and its products, where a single category can
   * contain multiple products. The association is managed by the 'category' field
   * within the Product entity.
   */
  @OneToMany(mappedBy = "category")
  private List<Product> products;


}
