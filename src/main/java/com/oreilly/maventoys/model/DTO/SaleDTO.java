package com.oreilly.maventoys.model.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object for Sale transactions.
 * Captures essential details of sales transactions for reporting, analysis, and operational management.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)// esta anotacion aunque suena confusa se encarga de no poner los nulls
public class SaleDTO {


  /**
   * Unique identifier for the sale transaction.
   */
  private Integer id;


  /**
   * Identifier for the store where the sale transaction took place, linking the sale to its physical location.
   */
  private Integer storeId;


  /**
   * Total amount of the sale transaction, representing the financial value of the transaction.
   */
  private Double total;


  /**
   * Date when the sale transaction occurred, important for temporal analysis and record-keeping.
   */
  private LocalDate date;


  /**
   * Identifier for the employee who facilitated or was responsible for the sale transaction, providing a link
   * to the individual contributing to sales performance.
   */
  private Integer employeeId;
  /**
   * Identifier for the employee who facilitated or was responsible for the sale transaction, providing a link
   * to the individual contributing to sales performance.
   */
  //TODO cambiar comentario.
  private List<InvoicesDTO> products;


  //get y set
}
