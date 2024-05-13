package com.oreilly.maventoys.repository;


import com.oreilly.maventoys.model.entity.Product;

import com.oreilly.maventoys.model.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link Product} entities. Provides standard CRUD functionalities
 * with {@link JpaRepository} and adds custom queries for product retrieval by active status,
 * category, and sales history.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {

  /**
   * Finds products by their active status. This allows for filtering products that are
   * currently available for sale or have been discontinued.
   *
   * @return A list of products matching the active status.
   */
  List<Product> getByActiveTrue();


  /**
   * Retrieves all sales transactions associated with a given product ID.
   * This can be used to analyze the sales performance of specific products.
   *
   * @param productId The product ID for which sales are queried.
   *
   * @return A list of {@link Sale} associated with the product.
   */
  @Query("SELECT s FROM Sale s JOIN s.invoices i WHERE i.product.id = :productId")
  List<Sale> getAllSalesByProductId(@Param("productId") Integer productId);


  /**
   * Finds products by their category ID, aiding in category-based product retrieval.
   *
   * @param categoryId The ID of the category.
   *
   * @return A list of products belonging to the specified category.
   */
  List<Product> getByCategoryId(Integer categoryId);


  /**
   * Finds products by name with a custom query that also orders the results by
   * creation date in descending order, helping in retrieving the latest products first.
   *
   * @param name The name of the product.
   *
   * @return A list of products matching the name, ordered by creation date.
   */
  @Query(value = "SELECT p FROM Product p WHERE p.name = :name ORDER BY p.creationDate DESC")
  List<Product> getProductByName(@Param("name") String name);

  /**
   * Retrieves a list of the top 5 best-selling products within a specific category. This method executes a native
   * SQL query
   * to aggregate sales data from the invoices and products tables, ranking the products based on the total quantity
   * sold.
   * The query filters products by the specified category ID, groups the results by product ID, and sorts them in
   * descending order
   * by the sum of quantities sold. It limits the result to the top 5 products to identify the bestsellers in that
   * category.
   *
   * @param categoryId The ID of the category for which to find the best-selling products. Products outside this
   *                   category are excluded from the results.
   *
   * @return A list of {@link Product} objects representing the top 5 best-selling products in the specified category
   * . Each product includes its ID, name, cost, price, category ID, active status, creation date, and the aggregated
   * quantity sold (labeled as 'top'). If there are fewer than 5 products in the category, the method returns only
   * those that match the criteria.
   */
  @Query(value =
      "select p.id, p.name, p.cost, p.price, p.category_id, p.active, p.creation_date, sum(i.quantity) as top " +
          "from products p " + "join invoices i on p.id = i.product_id " + "where p.category_id = ?1 " +
          "group by p.id " + "order by top desc " + "limit 5", nativeQuery = true)
  List<Product> findBestSellersByCategory(int categoryId);
}
