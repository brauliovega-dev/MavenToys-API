package com.oreilly.maventoys.repository;

import com.oreilly.maventoys.model.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link Inventory} entities. Extends {@link JpaRepository}
 * to provide standard CRUD operations. Includes custom queries for inventory management, such as
 * finding stock levels by product ID.
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

  /**
   * Retrieves the stock level for a specific product based on its ID.
   * This query is useful for inventory checks and stock management.
   *
   * @param productId The ID of the product whose stock level is queried.
   *
   * @return The stock on hand for the specified product.
   */
  @Query("SELECT i.stockOnHand FROM Inventory i WHERE i.product.id = :productId")
  Integer getStockByProductId(@Param("productId") Integer productId);

}
