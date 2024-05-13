package com.oreilly.maventoys.mapper;

import com.oreilly.maventoys.model.DTO.ProductDTO;
import com.oreilly.maventoys.model.entity.Product;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * MapStruct mapper interface for converting between {@link Product} entities and {@link ProductDTO} data transfer
 * objects.
 * This mapper handles the conversion of product details, including custom mappings for nested properties such as
 * category IDs,
 * and supports methods for directly updating entities from DTOs.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {

  /**
   * Converts a Product entity to a ProductDTO, including a custom mapping for the category ID.
   *
   * @param product The Product entity to convert.
   *
   * @return The converted ProductDTO with the category ID mapped from the Product's associated category.
   */
  @Mapping(source = "category.id", target = "categoryId")
  @Mapping(target = "stockOnHand", ignore = true)
  ProductDTO productToProductDTO(Product product);

  /**
   * Converts a ProductDTO object to a Product object.
   *
   * @param productDTO The ProductDTO to be converted.
   *
   * @return The converted Product object.
   */
  Product productDTOToProduct(ProductDTO productDTO);


  /**
   * Updates an existing Product entity with values from a ProductDTO.
   * Only non-null fields in the DTO are used to update the entity, with specific fields like 'id' and 'creationDate'
   * ignored.
   *
   * @param dto    The source ProductDTO containing updated values.
   * @param entity The target Product entity to be updated.
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "creationDate", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy =
      NullValueCheckStrategy.ALWAYS)
  void updateProductFromDto(ProductDTO dto, @MappingTarget Product entity);


}
