package com.oreilly.maventoys.repository.specifications;

import com.oreilly.maventoys.model.entity.Category;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * The CategorySpec class implements the Specification interface for the Category entity.
 * It is used to add additional constraints to a CriteriaQuery for Categories based on the Category's id and name.
 * This class uses Lombok's @AllArgsConstructor to automatically generate a constructor with parameters for all fields.
 */
@AllArgsConstructor
public class CategorySpec implements Specification<Category> {
  /**
   * The id of the Category for which the Specification is to be created.
   * It is used to add a predicate to the CriteriaQuery to check if the Category's id is equal to this id.
   * Can be null, in which case no id-based predicate is added.
   */
  private Integer id;

  /**
   * The name of the Category for which the Specification is to be created.
   * It is used to add a predicate to the CriteriaQuery to check if the Category's name is equal to this name.
   * Can be null, in which case no name-based predicate is added.
   */
  private String name;

  /**
   * This method is part of the Specification interface and is used to create a Predicate (a boolean-valued function)
   * that can be used in a CriteriaQuery to filter Categories based on their id and name.
   *
   * @param root            The root type in the from clause. It is used to form the path expressions in the Predicate.
   * @param query           The CriteriaQuery for which the Predicate is being formed.
   * @param criteriaBuilder Used to construct the Predicate.
   *
   * @return A Predicate that tests whether a Category's id is equal to this.id and its name is equal to this.name.
   */
  @Override
  public Predicate toPredicate(final Root<Category> root, final CriteriaQuery<?> query,
                               final CriteriaBuilder criteriaBuilder) {
    List<Predicate> predicates = new java.util.ArrayList<>();
    if (id != null) {
      predicates.add(criteriaBuilder.equal(root.get("id"), id));
    }
    if (name != null) {
      predicates.add(criteriaBuilder.equal(root.get("name"), name));
    }
    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
  }
}
