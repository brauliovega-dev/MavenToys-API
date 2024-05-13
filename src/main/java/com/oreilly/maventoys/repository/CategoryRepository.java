package com.oreilly.maventoys.repository;

import com.oreilly.maventoys.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link Category} entities. Provides standard CRUD operations
 * by extending {@link JpaRepository}. This interface includes additional methods for querying
 * {@link Category} entities based on their active status.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>, JpaSpecificationExecutor<Category> {

  /**
   * Finds all categories that match the specified active status.
   * This method allows for the dynamic retrieval of categories based on their
   * operational status within the application, enabling features such as filtering
   * active or inactive categories for administrative or display purposes.
   *
   * @return A list of {@link Category} entities that match the specified active status.
   */
  List<Category> findByActiveTrue();

  /**
   * Retrieves a summary of total sales by category for all active categories and products.
   * This query performs a JOIN operation across multiple tables: categories, products, invoices, and sales
   * to calculate the sum of sales totals. Only active categories and products are considered in this summary.
   *
   * The results are grouped by the category ID and name, ensuring that the sales totals are aggregated
   * at the category level. The final output is sorted in descending order based on the total sales amount,
   * allowing quick identification of the highest-selling categories.
   *
   * @return A list of objects (or a custom DTO projection) containing the category ID, category name,
   *         and the total sales amount for each active category. The list is sorted by total sales in
   *         descending order.
   * @query This method is annotated with @Query to define a native SQL query. The query is specified
   *        as a multiline string within the annotation's value attribute. It's marked as a nativeQuery
   *        to indicate that it uses pure SQL rather than JPQL. This approach is useful for complex queries
   *        that are not easily expressed or optimized with JPQL.
   */
  @Query(value = """
      SELECT
          c.id AS CategoryID,
          c.name AS CategoryName,
          SUM(s.total) AS TotalSales
      FROM
          categories c
      JOIN
          products p ON c.id = p.category_id
      JOIN
          invoices i ON p.id = i.product_id
      JOIN
          sales s ON i.sales_id = s.id
      WHERE
          c.active = TRUE AND
          p.active = TRUE
      GROUP BY
          c.id, c.name
      ORDER BY
          TotalSales DESC
     """, nativeQuery = true)
  List<Object[]> findCategorySales();

}
