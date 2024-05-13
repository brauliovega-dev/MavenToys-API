package com.oreilly.maventoys.mapper;

import com.oreilly.maventoys.model.DTO.EmployeeDTO;
import com.oreilly.maventoys.model.entity.Employee;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;


/**
 * MapStruct mapper interface for converting between {@link Employee} entities and {@link EmployeeDTO} data transfer
 * objects.
 * Includes custom mappings to handle the transformation of nested properties and provides a method for updating
 * entities based on DTOs, supporting a clean separation between entity and DTO layers.
 */
@Mapper(componentModel = "spring")
public interface EmployeeMapper {

  /**
   * Converts an Employee entity to an EmployeeDTO, with a custom mapping for the store ID.
   *
   * @param employee The Employee entity to convert.
   *
   * @return The converted EmployeeDTO with the store ID mapped from the Employee's associated store.
   */
  @Mapping(source = "store.id", target = "storeId")
  EmployeeDTO employeeToEmployeeDTO(Employee employee);

  /**
   * Converts an EmployeeDTO to an Employee entity, with a custom mapping for the store association based on store ID.
   *
   * @param employeeDTO The EmployeeDTO to convert.
   *
   * @return The converted Employee entity with its store association set based on the storeId in the DTO.
   */
  @Mapping(source = "storeId", target = "store.id")
  Employee employeeDTOToEmployee(EmployeeDTO employeeDTO);

  /**
   * Updates an existing Employee entity with values from an EmployeeDTO.
   * This method allows for selective updates, where only non-null fields in the DTO are used to update the entity.
   *
   * @param dto    The source EmployeeDTO containing updated values.
   * @param entity The target Employee entity to be updated.
   * @return The converted Employee entity with its store association set based on the storeId in the DTO.
   */
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy =
      NullValueCheckStrategy.ALWAYS)
  Employee updateEmployeeFromDto(EmployeeDTO dto, @MappingTarget Employee entity);

}
