package com.oreilly.maventoys.model.DTO;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Data Transfer Object for Employee information.
 * Facilitates the efficient and secure exchange of employee data across different layers of the application,
 * ensuring that sensitive details are encapsulated or omitted as necessary.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeDTO {
  /**
   * Unique identifier for the employee.
   */
  private Integer id;


  /**
   * First name of the employee.
   */
  private String firstName;


  /**
   * Last name of the employee.
   */
  private String lastName;


  /**
   * Date when the employee was hired. This is important for calculating tenure and eligibility for certain benefits.
   */
  private LocalDate hireDate;


  /**
   * Gender of the employee. This information can be used for demographic analysis and reporting.
   */
  private String gender;


  /**
   * Birthdate of the employee. Used for age verification and may influence benefits and retirement planning.
   */
  private LocalDate birthDate;


  /**
   * Indicates whether the employee is currently active. Active employees are considered part of the workforce,
   * while inactive employees may be on leave or have left the company.
   */
  private boolean active;


  /**
   * Identifier for the store the employee is associated with. This is used to establish the relationship
   * between an employee and their primary place of work within the company's operations.
   */
  private Integer storeId;

  /**
   * The number of sales transactions.
   */
  private Long numberOfSales;
}

