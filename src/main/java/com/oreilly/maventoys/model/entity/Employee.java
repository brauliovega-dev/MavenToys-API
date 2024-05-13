package com.oreilly.maventoys.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * Entity representing an employee within the application. Employees are associated with stores
 * and are linked to sales, reflecting their role in the business operations. This entity
 * captures essential employee information including personal details and employment status.
 */
@Entity
@Getter
@Setter
@Table(name = "employees")
public class Employee {

  /**
   * Unique identifier for the employee. This ID is generated automatically
   * and is used to uniquely identify an employee within the database.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * First name of the employee. This is used to personally identify the employee
   * within the application's user interface and in reports.
   */
  @Column(name = "first_name")
  private String firstName;

  /**
   * Last name of the employee. It complements the first name to fully identify the
   * employee within the business operations and records.
   */
  @Column(name = "last_name")
  private String lastName;

  /**
   * The date the employee was hired by the company. This is important for tracking
   * employment duration and for various HR and operational purposes.
   */
  @Column(name = "hire_date")
  @Temporal(TemporalType.DATE)
  private LocalDate hireDate;

  /**
   * Gender of the employee. Used for demographic analysis and reporting, as well as
   * ensuring equality and diversity within the workplace.
   */
  private String gender;

  /**
   * Birthdate of the employee. This information is used for HR purposes, including
   * benefits administration and compliance with labor laws.
   */
  @Column(name = "birth_date")
  @Temporal(TemporalType.DATE)
  private LocalDate birthDate;


  /**
   * The store to which the employee is assigned. This many-to-one relationship links
   * an employee to a specific store, indicating their primary place of work.
   */
  @ManyToOne
  @JoinColumn(name = "store_id")
  private Store store;

  /**
   * Flag indicating whether the employee is currently active. Active employees are
   * considered part of the current workforce, while inactive employees may be on leave
   * or no longer employed.
   */
  private Boolean active; // Asignar un valor predeterminado

  /**
   * List of sales attributed to the employee. This establishes a one-to-many
   * relationship between an employee and sales, reflecting the employee's role
   * in generating business for the company.
   */
  @OneToMany(mappedBy = "employee")
  private List<Sale> sales;


  // Getters and setters

}
