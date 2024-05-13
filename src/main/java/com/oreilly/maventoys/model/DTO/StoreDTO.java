package com.oreilly.maventoys.model.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Data Transfer Object for Store information.
 * Conveys essential details of stores for use in managing store records, location-based reporting, and operational
 * oversight.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreDTO {


  /**
   * Unique identifier for the store.
   */
  private int id;


  /**
   * Name of the store, used for identification and as part of the store's address and contact information.
   */
  private String name;


  /**
   * City where the store is located, aiding in geographical categorization and marketing activities.
   */
  private String city;


  /**
   * Precise location of the store, including address details for navigation and logistical purposes.
   */
  private String location;


  /**
   * Date when the store was opened, relevant for anniversaries, historical analysis, and strategic planning.
   */
  private LocalDate openDate;


  /**
   * Flag indicating whether the store is currently active and operational.
   */
  private Boolean active;

  /**
   * The total sales amount.
   * This field holds the sum of sales transactions in a given period, or the total sales volume for a specific metric,
   * such as a store or an employee, depending on the context where it's used.
   */
  private Double totalSales;
  //get y set
}
