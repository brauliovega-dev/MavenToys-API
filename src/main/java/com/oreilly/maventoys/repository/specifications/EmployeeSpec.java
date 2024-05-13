package com.oreilly.maventoys.repository.specifications;

import com.oreilly.maventoys.model.entity.Employee;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * The EmployeeSpec class implements the Specification interface for the Employee entity.
 * It is used to add additional constraints to a CriteriaQuery for Employees based on the Employee's id, first name,
 * and last name.
 * This class uses Lombok's @AllArgsConstructor to automatically generate a constructor with parameters for all fields.
 */
@AllArgsConstructor
public class EmployeeSpec implements Specification<Employee> {
  /**
   * The id of the Employee for which the Specification is to be created.
   * It is used to add a predicate to the CriteriaQuery to check if the Employee's id is equal to this id.
   * Can be null, in which case no id-based predicate is added.
   */
  private Integer id;

  /**
   * The first name of the Employee for which the Specification is to be created.
   * It is used to add a predicate to the CriteriaQuery to check if the Employee's first name contains this firstName.
   * Can be null, in which case no first name-based predicate is added.
   */
  private String firstName;

  /**
   * The last name of the Employee for which the Specification is to be created.
   * It is used to add a predicate to the CriteriaQuery to check if the Employee's last name contains this lastName.
   * Can be null, in which case no last name-based predicate is added.
   */
  private String lastName;

  /**
   * This method is part of the Specification interface and is used to create a Predicate (a boolean-valued function)
   * that can be used in a CriteriaQuery to filter Employees based on their id, first name, and last name.
   *
   * @param root            The root type in the from clause. It is used to form the path expressions in the Predicate.
   * @param query           The CriteriaQuery for which the Predicate is being formed.
   * @param criteriaBuilder Used to construct the Predicate.
   *
   * @return A Predicate that tests whether an Employee's id is equal to this.id, its first name contains this
   * .firstName, and its last name contains this.lastName.
   */
  @Override
  public Predicate toPredicate(final Root<Employee> root, final CriteriaQuery<?> query,
                               final CriteriaBuilder criteriaBuilder) {
    List<Predicate> predicates = new ArrayList<>();

    if (id != null) {
      predicates.add(criteriaBuilder.equal(root.get("id"), id));
    }

    if (firstName != null) {
      predicates.add(
          criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%"));
    }

    if (lastName != null) {
      predicates.add(
          criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%"));
    }
    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
  }
}
