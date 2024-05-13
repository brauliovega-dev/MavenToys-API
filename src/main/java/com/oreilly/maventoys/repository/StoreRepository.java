package com.oreilly.maventoys.repository;

import com.oreilly.maventoys.model.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data repository interface for {@link Store} entities. Extends {@link JpaRepository}
 * to provide standard CRUD operations. This repository also includes custom functionality
 * for querying stores based on their active status.
 */
@Repository
public interface StoreRepository extends JpaRepository<Store, Integer>, JpaSpecificationExecutor<Store> {

    /**
     * Retrieves a list of stores by their active status.
     *
     * @return List of Store objects filtered by the active status.
     */
    List<Store> getByActiveTrue();

    /**
     * Retrieves a list of the top 5 selling stores based on total sales.
     * This method executes a native SQL query to join the {@code stores} and {@code sales} tables,
     * summing up the total sales per store, grouping the results by store ID, and ordering them
     * in descending order of total sales.
     *
     * <p>The result is a list of object arrays where each array contains the store's ID, store's name,
     * and the total sales for that store. This allows for easy identification of the top-performing stores
     * in terms of sales volume.</p>
     *
     * @return a list of {@link Object[]} where each Object[] contains:
     *         <ol>
     *             <li><strong>Store ID:</strong> The unique identifier of the store.</li>
     *             <li><strong>Store Name:</strong> The name of the store.</li>
     *             <li><strong>Total Sales:</strong> The total sales amount of the store.</li>
     *         </ol>
     *         The list is limited to the top 5 stores with the highest total sales.
     */
    @Query(value = "SELECT s.id, s.name, SUM(sa.total) AS total_sales " +
        "FROM stores s " +
        "JOIN sales sa ON s.id = sa.store_id " +
        "GROUP BY s.id " +
        "ORDER BY total_sales DESC " +
        "LIMIT 5", nativeQuery = true)
    List<Object[]> findStoresTopSellers();


}
