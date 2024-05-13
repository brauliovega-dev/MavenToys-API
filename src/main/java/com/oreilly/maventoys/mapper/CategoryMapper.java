package com.oreilly.maventoys.mapper;

import com.oreilly.maventoys.model.DTO.CategoryDTO;
import com.oreilly.maventoys.model.entity.Category;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * MapStruct mapper interface for converting between {@link Category} entities and {@link CategoryDTO} data transfer
 * objects.
 * Provides methods for direct mapping and updating entities based on DTOs, facilitating the separation of entity
 * and DTO layers in the application architecture.
 */

@Mapper(componentModel = "spring")
public interface CategoryMapper {

  /**
   * Converts a Category entity to a CategoryDTO.
   *
   * @param category The Category entity to convert.
   *
   * @return The converted CategoryDTO.
   */
  CategoryDTO categoryToCategoryDTO(Category category);

  /**
   * Converts a CategoryDTO to a Category entity.
   *
   * @param categoryDTO The CategoryDTO to convert.
   *
   * @return The converted Category entity.
   */
  Category categoryDTOToCategory(CategoryDTO categoryDTO);

  /**
   * Updates an existing Category entity with values from a CategoryDTO.
   * Fields in the entity that are not present in the DTO are ignored, preventing nullification.
   *
   * @param dto    The source CategoryDTO containing updated values.
   * @param entity The target Category entity to be updated.
   */
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy =
      NullValueCheckStrategy.ALWAYS)
  void updateCategoryFromDto(CategoryDTO dto, @MappingTarget Category entity);
}
