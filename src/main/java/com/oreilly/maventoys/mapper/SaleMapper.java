package com.oreilly.maventoys.mapper;

import com.oreilly.maventoys.model.DTO.SaleDTO;
import com.oreilly.maventoys.model.entity.Sale;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * MapStruct mapper interface for converting between Sale entity and SaleDTO.
 * This mapper handles the conversion of ID references for store and employee
 * between the DTO and the entity. This is crucial for maintaining the
 * relationships in the entity model when transferring data to and from the DTO.
 */
@Mapper(componentModel = "spring")
public interface SaleMapper {

  /**
   * Converts a SaleDTO to a Sale entity.
   * This method maps the storeId and employeeId from the DTO to the respective
   * ID fields in the Store and Employee associations in the Sale entity.
   *
   * @param saleDTO the DTO containing sale data
   *
   * @return a Sale entity with associated store and employee based on the DTO
   */
  @Mapping(target = "store.id", source = "storeId")
  @Mapping(target = "employee.id", source = "employeeId")
  Sale saleDTOToSale(SaleDTO saleDTO);

  /**
   * Converts a Sale entity to a SaleDTO.
   * This method extracts the IDs from the Store and Employee associations in the
   * entity to populate the storeId and employeeId fields in the DTO.
   *
   * @param sale the entity containing sale information
   *
   * @return a SaleDTO with storeId and employeeId fields set from the entity
   */
  @Mapping(source = "store.id", target = "storeId")
  @Mapping(source = "employee.id", target = "employeeId")
  @Mapping(target = "products", ignore = true)
  SaleDTO saleToSaleDTO(Sale sale);

  /**
   * Updates a Sale entity with data from a SaleDTO.
   * Only non-null properties in the SaleDTO are applied to the entity.
   * This method is useful for patch updates.
   *
   * @param saleDTO the DTO containing updated sale data
   * @param sale    the entity to be updated
   */
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy =
      NullValueCheckStrategy.ALWAYS)
  void updateSaleFromDto(SaleDTO saleDTO, @MappingTarget Sale sale);
}
