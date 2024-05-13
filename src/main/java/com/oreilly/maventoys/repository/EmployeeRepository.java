package com.oreilly.maventoys.repository;

import com.oreilly.maventoys.model.entity.Employee;
import com.oreilly.maventoys.model.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link Employee} entities. Extends {@link JpaRepository}
 * to provide basic CRUD operations and includes custom methods for querying employees
 * based on store affiliation, active status, and their sales records.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer>, JpaSpecificationExecutor<Employee> {

  /**
   * Finds all employees associated with a specific store by the store's ID.
   *
   * @param storeId The ID of the store.
   *
   * @return A list of {@link Employee} entities associated with the specified store.
   */
  List<Employee> getEmplotesByStoreId(Integer storeId);


  /**
   * Retrieves all active employees along with their associated stores to optimize loading.
   * Uses a JOIN FETCH clause to load the store associated with each active employee in a single query,
   * reducing the number of database hits required to fetch this associated data.
   *
   * @return A list of active {@link Employee} entities with their associated stores.
   */
  @Query("SELECT e " + "FROM Employee e " + "JOIN FETCH e.store " + "WHERE e.active = true")
  List<Employee> getByActiveTrue();


  /**
   * Finds all sales records by an employee's ID, facilitating the tracking of sales activities
   * and performance for individual employees.
   *
   * @param employeeId The ID of the employee whose sales records are to be retrieved.
   *
   * @return A list of {@link Sale} made by the specified employee.
   */
  @Query("SELECT s FROM Sale s WHERE s.employee.id = :employeeId")
  List<Sale> getAllSalesByEmployeeId(@Param("employeeId") Integer employeeId);


  /**
   * Retrieves a paginated list of active employees along with their associated stores,
   * similar to {@link #getActiveEmployeesWithStores()} but with pagination support.
   * This method is ideal for applications with a large number of employees, improving
   * performance and user experience by limiting the amount of data loaded at once.
   *
   * @param pageable Pagination information.
   *
   * @return A page of active {@link Employee} entities with their associated stores.
   */
  @Query("SELECT e FROM Employee e JOIN FETCH e.store WHERE e.active = true")
  Page<Employee> findAllActiveEmployeesWithStores(Pageable pageable);


  /**
   * Retrieves a list of the top 10 selling employees along with their store ID and the total number of sales.
   * <p>
   * This method executes a native SQL query to select the employee's ID, first name, last name, store ID,
   * and the count of sales associated with each employee. The results are grouped by the employee's ID,
   * first name, last name, and store ID, and are ordered in descending order by the count of sales to
   * identify the top sellers. Each record in the returned list includes the employee's ID, first name,
   * last name, store ID, and the total number of sales, represented as an array of objects.
   * </p>
   * <p>
   * Note: This method uses a native query, which means it directly executes the specified SQL query on the database.
   * </p>
   *
   * @return a {@link List} of {@link Object[]} where each {@link Object[]} represents an employee's ID (index 0),
   *         first name (index 1), last name (index 2), store ID (index 3), and the count of their total sales
   *         (index 4). The list is limited to the top 10 employees based on the number of sales.
   */
  @Query(value = "SELECT e.id, e.first_name, e.last_name, e.store_id, COUNT(s.id) AS numberOfSales " +
      "FROM employees e " +
      "JOIN sales s ON e.id = s.employee_id " +
      "GROUP BY e.id, e.first_name, e.last_name, e.store_id " +
      "ORDER BY COUNT(s.id) DESC " +
      "LIMIT 5", nativeQuery = true)
  List<Object[]> findTopSellersWithStoreId();



}
