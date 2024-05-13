package com.oreilly.maventoys.repository.specifications;

import com.oreilly.maventoys.model.entity.Sale;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * The SaleSpec class implements the Specification interface for the Sale entity.
 * It is used to add additional constraints to a CriteriaQuery for Sales based on the Sale's id, storeId, and
 * employeeId.
 * This class uses Lombok's @AllArgsConstructor to automatically generate a constructor with parameters for all fields.
 */
@AllArgsConstructor
public class SaleSpec implements Specification<Sale> {
  /**
   * The unique identifier of the sale. If specified, adds a predicate to filter by this ID.
   */
  private Integer id;

  /**
   * The unique identifier of the store associated with the sale. If specified, adds a predicate to filter sales by
   * this store ID.
   */
  private Integer storeId;

  /**
   * The unique identifier of the employee associated with the sale. If specified, adds a predicate to filter sales
   * by this employee ID.
   */
  private Integer employeeId;


  /**
   * Constructs a predicate for a criteria query filtering {@code Sale} entities based on their ID.
   * This method builds a list of predicates based on provided entity attributes and compiles them into
   * a single predicate using the conjunction (AND) operator. If the {@code id} attribute is not null,
   * it adds a predicate to check if the {@code Sale} entity's ID matches the given ID.
   *
   * @param root            the root type in the query from which the {@code Sale} entities are selected
   * @param query           the criteria query being constructed
   * @param criteriaBuilder used to construct the {@link Predicate} for the criteria query
   *
   * @return a {@link Predicate} object representing the conjunction of all criteria; never null, but possibly a
   * no-op (always-true predicate) if no criteria are specified (e.g., if {@code id} is null)
   */
  @Override
  public Predicate toPredicate(final Root<Sale> root, final CriteriaQuery<?> query,
                               final CriteriaBuilder criteriaBuilder) {
    List<Predicate> predicates = new ArrayList<>();
    if (id != null) {
      predicates.add(criteriaBuilder.equal(root.get("id"), id));
    }
    if (storeId != null) {
      predicates.add(criteriaBuilder.equal(root.get("store").get("id"), storeId));
    }
    if (employeeId != null) {
      predicates.add(criteriaBuilder.equal(root.get("employee").get("id"), employeeId));
    }
    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
  }

}
