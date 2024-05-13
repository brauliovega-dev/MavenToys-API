package com.oreilly.maventoys.repository.specifications;

import com.oreilly.maventoys.model.entity.Store;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * The StoreSpec class implements the Specification interface for the Store entity.
 * It is used to add additional constraints to a CriteriaQuery for Stores based on the Store's id, name, and location.
 * This class uses Lombok's @AllArgsConstructor to automatically generate a constructor with parameters for all fields.
 */
@AllArgsConstructor
public class StoreSpec implements Specification<Store> {
  /**
   * The unique identifier of the store. If specified, adds a predicate to filter by this ID.
   */
  private Integer id;

  /**
   * The name of the store. If specified and not empty, adds a predicate to filter by store
   * names that contain the provided name, case-insensitive.
   */
  private String name;

  /**
   * The location of the store. If specified and not empty, adds a predicate to filter by
   * store locations that contain the provided location, case-insensitive.
   */
  private String location;


  /**
   * Constructs a predicate for a criteria query filtering {@code Store} entities based on their ID, name, and location.
   * This method builds a list of predicates based on provided entity attributes and compiles them into
   * a single predicate using the conjunction (AND) operator. If the {@code id} attribute is not null,
   * it adds a predicate to check if the {@code Store} entity's ID matches the given ID.
   *
   * @param root            the root type in the query from which the {@code Store} entities are selected
   * @param query           the criteria query being constructed
   * @param criteriaBuilder used to construct the {@link Predicate} for the criteria query
   *
   * @return a {@link Predicate} object representing the conjunction of all criteria; never null, but possibly a
   * no-op (always-true predicate) if no criteria are specified (e.g., if {@code id} is null)
   */
  @Override
  public Predicate toPredicate(final Root<Store> root, final CriteriaQuery<?> query,
                               final CriteriaBuilder criteriaBuilder) {
    List<Predicate> predicates = new ArrayList<>();

    if (id != null) {
      predicates.add(criteriaBuilder.equal(root.get("id"), id));
    }
    if (name != null && !name.isEmpty()) {
      predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
    }
    if (location != null && !location.isEmpty()) {
      predicates.add(
          criteriaBuilder.like(criteriaBuilder.lower(root.get("location")), "%" + location.toLowerCase() + "%"));
    }

    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
  }
}


