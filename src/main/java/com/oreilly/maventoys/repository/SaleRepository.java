package com.oreilly.maventoys.repository;

import com.oreilly.maventoys.model.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link Sale} entities. Extends {@link JpaRepository} to provide
 * comprehensive CRUD operations and includes custom queries for analyzing sales by store,
 * employee, and over time periods.
 */
@Repository
public interface SaleRepository extends JpaRepository<Sale, Integer>, JpaSpecificationExecutor<Sale> {

  /**
   * Finds sales transactions by store ID, useful for reporting and analyzing sales
   * performance by location.
   *
   * @param storeId The ID of the store.
   *
   * @return A list of sales transactions for the specified store.
   */
  List<Sale> getByStoreId(Integer storeId);


  /**
   * Calculates the sum of total sales amounts for a given store ID. This aggregate function
   * is beneficial for financial summaries and sales performance analysis by store.
   *
   * @param storeId The ID of the store for which the sales total is calculated.
   *
   * @return The sum of total sales amounts for the store.
   */
  @Query("SELECT SUM(s.total) FROM Sale s WHERE s.store.id = :storeId")
  Optional<Double> getTotalByStoreId(Integer storeId);


  /**
   * Finds sales transactions by employee ID, assisting in evaluating individual
   * employee sales performance.
   *
   * @param employeeId The ID of the employee.
   *
   * @return A list of sales attributed to the employee.
   */
  List<Sale> findByEmployeeId(Integer employeeId);


  /**
   * Retrieves sales transactions occurring within a specified date range. This is
   * essential for time-based sales analysis and reporting.
   *
   * @param startDate The start date of the period.
   * @param endDate   The end date of the period.
   *
   * @return A list of sales transactions within the specified date range.
   */
  List<Sale> findByDateBetween(LocalDate startDate, LocalDate endDate);


}
