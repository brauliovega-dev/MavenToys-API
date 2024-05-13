package com.oreilly.maventoys.mapper;

import com.oreilly.maventoys.model.DTO.StoreDTO;
import com.oreilly.maventoys.model.entity.Store;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * MapStruct mapper interface for converting between {@link Store} entities and {@link StoreDTO} data transfer objects.
 * Facilitates the seamless transformation of store data for use in various layers of the application, ensuring a
 * clear separation
 * between the entity model and data transfer objects. Includes functionality to update store entities based on DTO
 * data.
 */
@Mapper(componentModel = "spring")
public interface StoreMapper {

  /**
   * Converts a Store entity to a StoreDTO.
   *
   * @param store The Store entity to convert.
   *
   * @return The converted StoreDTO.
   */
  StoreDTO storeToStoreDTO(Store store);


  /**
   * Converts a StoreDTO to a Store entity.
   *
   * @param storeDTO The StoreDTO to convert.
   *
   * @return The converted Store entity.
   */
  Store storeDTOToStore(StoreDTO storeDTO);


  /**
   * Updates an existing Store entity with values from a StoreDTO, excluding 'id' and 'openDate' from updates.
   * This approach prevents overwriting the store's identity and open date during updates.
   *
   * @param dto    The source StoreDTO containing updated values.
   * @param entity The target Store entity to be updated.
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "openDate", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy =
      NullValueCheckStrategy.ALWAYS)
  void updateStoreFromDto(StoreDTO dto, @MappingTarget Store entity);


}
