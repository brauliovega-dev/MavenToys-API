package com.oreilly.maventoys.repository.specifications;

import com.oreilly.maventoys.model.entity.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor

public class ProductSpec implements Specification<Product> {
  /**
   * The unique identifier for the user.
   */
  private Integer id;

  /**
   * The name of the product.
   */
  private String name;

  /**
   * Builds a {@link Predicate} for a criteria query filtering {@link Product} entities based on specified conditions.
   * <p>
   * This method constructs a list of {@link Predicate} conditions to apply to a query of {@link Product} entities,
   * using the {@link CriteriaBuilder} and the {@link Root} of the entities. It supports filtering by 'id' and 'name'.
   * </p>
   *
   * @param root            the root of the query, correlates to the {@link Product} entity.
   * @param query           the criteria query being constructed, not modified in this method.
   * @param criteriaBuilder the criteria builder used to create {@link Predicate} instances.
   *
   * @return a composite {@link Predicate} combining all conditions with a logical AND operation, or null if no
   * conditions are specified.
   *
   * @override
   */
  @Override
  public Predicate toPredicate(final Root<Product> root, final CriteriaQuery<?> query,
                               final CriteriaBuilder criteriaBuilder) {
    List<Predicate> predicates = new ArrayList<>();
    if (id != null) {
      predicates.add(criteriaBuilder.equal(root.get("id"), id));
    }
    if (name != null) {
      predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
    }
    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
  }

}
