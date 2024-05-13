package com.oreilly.maventoys.service;

import com.oreilly.maventoys.model.DTO.EmployeeDTO;
import com.oreilly.maventoys.model.DTO.SaleDTO;
import com.oreilly.maventoys.model.DTO.StoreDTO;
import com.oreilly.maventoys.exceptions.GeneralException;
import com.oreilly.maventoys.exceptions.IdNotFound;
import com.oreilly.maventoys.mapper.EmployeeMapper;
import com.oreilly.maventoys.mapper.SaleMapper;
import com.oreilly.maventoys.mapper.StoreMapper;
import com.oreilly.maventoys.model.entity.Employee;
import com.oreilly.maventoys.model.entity.Sale;
import com.oreilly.maventoys.model.entity.Store;
import com.oreilly.maventoys.repository.EmployeeRepository;
import com.oreilly.maventoys.repository.SaleRepository;
import com.oreilly.maventoys.repository.StoreRepository;
import com.oreilly.maventoys.model.CustomApiResponse;
import com.oreilly.maventoys.repository.specifications.StoreSpec;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for handling operations related to stores.
 * This includes creating, updating, retrieving, and deleting store records.
 * It uses StoreRepository, EmployeeRepository, and SaleRepository for persistence operations
 * and StoreMapper, EmployeeMapper, and SaleMapper for converting between entity and DTO representations.
 */
@Service
@RequiredArgsConstructor

public class StoreService {
  /**
   * Repository for managing store data.
   */
  private final StoreRepository storeRepository;

  /**
   * Repository for managing employee data.
   */
  private final EmployeeRepository employeeRepository;

  /**
   * Repository for managing sales data.
   */
  private final SaleRepository saleRepository;

  /**
   * Mapper for managing employee data.
   */
  private final EmployeeMapper employeeMapper;

  /**
   * Mapper for managing store data.
   */
  private final StoreMapper storeMapper;

  /**
   * Mapper for managing store data.
   */
  private final SaleMapper saleMapper;


  /**
   * Fetches all stores marked as active within the database and converts them into a list of Data Transfer Objects
   * (DTOs).
   * This method filters stores based on their active status, ensuring only currently operational stores are included
   * in the results.
   * Each active store entity is mapped to a {@link StoreDTO} to facilitate easy data transfer and interaction with
   * other application layers or services.
   *
   * @return {@link <ApiResponse<List<StoreDTO>>> } containing a list of DTOs for all active stores and a status
   * indicating successful retrieval.
   *
   * @throws GeneralException if an unexpected error occurs during the process of fetching active stores from the
   *                          database.
   */
  public CustomApiResponse<List<StoreDTO>> getStores() {
    List<Store> activeStores;
    try {
      activeStores = storeRepository.getByActiveTrue();
    } catch (RuntimeException error) {
      throw new GeneralException("Error fetching all active stores: " + "CAUSE: " + error.getMessage(), error);
    }
    if (activeStores.isEmpty()) { // esta mal
      // la exepcion no seria una general esa se utiliza para los 500
      throw new EntityNotFoundException("No active stores found");
    }
    List<StoreDTO> storeDTOs = activeStores.stream().map(storeMapper::storeToStoreDTO).collect(Collectors.toList());
    return new CustomApiResponse<>("Active store details fetched successfully", storeDTOs);
  }

  /**
   * Retrieves the details of a specific store by its unique identifier (ID) and converts it to a DTO.
   * This method looks up a store by its ID, providing a way to access detailed information about a single store.
   * If the store is found, it is converted to a {@link StoreDTO}, otherwise, an IdNotFound exception is thrown.
   *
   * @param id The unique identifier of the store to retrieve.
   *
   * @return {@link CustomApiResponse <StoreDTO>} containing the DTO of the store and a success status if found.
   *
   * @throws IdNotFound       if no store with the specified ID can be found in the database.
   * @throws GeneralException if a problem is encountered during the retrieval process.
   */
  public CustomApiResponse<StoreDTO> getStoreById(final Integer id) {
    try {
      StoreDTO storeDTO = storeRepository.findById(id).map(storeMapper::storeToStoreDTO)
                                         .orElseThrow(() -> new IdNotFound("Store not found with ID: " + id));
      return new CustomApiResponse<>("Store details fetched successfully", storeDTO);
    } catch (GeneralException error) {
      throw new GeneralException(
          "A problem was encountered while retrieving the store. " + "CAUSE: " + error.getCause());
    }
  }

  /**
   * Creates a new store record in the database based on the provided DTO and returns the created store as a DTO.
   * This method converts a {@link StoreDTO} to a store entity, saves it in the database, and then converts the saved
   * entity
   * back to a DTO for the response. It is used to add new store records to the database.
   *
   * @param storeDTO The DTO containing the data to create a new store.
   *
   * @return {@link CustomApiResponse <StoreDTO>} containing the DTO of the newly created store and a success status.
   *
   * @throws GeneralException if an error occurs during the creation of the store record.
   */
  public CustomApiResponse<StoreDTO> createStore(final StoreDTO storeDTO) {
    if (storeDTO == null) {
      throw new IllegalArgumentException("StoreDTO cannot be null");
    }
    try {
      Store store = storeMapper.storeDTOToStore(storeDTO);
      store = storeRepository.save(store);
      StoreDTO resultDTO = storeMapper.storeToStoreDTO(store);
      return new CustomApiResponse<>("Store created successfully", resultDTO);
    } catch (Exception error) {
      throw new GeneralException("Error creating store: " + "CAUSE: " + error.getCause());
    }
  }


  /**
   * Partially updates an existing store's details with the provided DTO data for a specified store ID.
   * This method allows for selective update of store information, applying only the changes provided in the store DTO.
   * It's particularly useful for making incremental changes to a store's data without needing a full update.
   *
   * @param id       The ID of the store to be updated.
   * @param storeDTO The DTO containing the data to update the store with.
   *
   * @return {@link CustomApiResponse <StoreDTO>} containing the updated store DTO and a success status.
   *
   * @throws IdNotFound       if no store with the specified ID is found.
   * @throws GeneralException if an error occurs during the update process.
   */

  public CustomApiResponse<StoreDTO> patchStore(final int id, final StoreDTO storeDTO) {
    try {
      return storeRepository.findById(id).map(store -> {
        storeMapper.updateStoreFromDto(storeDTO, store);
        store = storeRepository.save(store);
        StoreDTO updatedStoreDTO = storeMapper.storeToStoreDTO(store);
        return new CustomApiResponse<>("Store updated successfully", updatedStoreDTO);
      }).orElseThrow(() -> new IdNotFound("Store not found with ID: " + id));
    } catch (Exception error) {
      throw new GeneralException("Error updating store: " + "CAUSE: " + error.getCause());
    }
  }


  /**
   * Fully updates an existing store's details based on the provided store DTO for the specified store ID.
   * This method overwrites the store's current data with the contents of the provided DTO, facilitating complete
   * updates
   * to store information as opposed to incremental patches.
   *
   * @param id       The ID of the store to be updated.
   * @param storeDTO The DTO containing the new data for the store.
   *
   * @return {@link CustomApiResponse <StoreDTO>} containing the DTO of the updated store and a success status.
   *
   * @throws IdNotFound       if no store with the specified ID is found.
   * @throws GeneralException if an error occurs during the update process.
   */
  public CustomApiResponse<StoreDTO> updateStore(final Integer id, final StoreDTO storeDTO) {
    Store store = storeRepository.findById(id).orElseThrow(() -> new IdNotFound("Store not found with ID: " + id));
    try {
      storeMapper.updateStoreFromDto(storeDTO, store);
      store = storeRepository.save(store);
      StoreDTO resultDTO = storeMapper.storeToStoreDTO(store);
      return new CustomApiResponse<>("Store updated successfully", resultDTO);
    } catch (Exception error) {
      throw new GeneralException("Error updating store: " + "CAUSE: " + error.getMessage());
    }
  }


  /**
   * Retrieves a list of employees working at a specific store by the store's ID, converting each to a DTO.
   * This method is useful for managing and viewing the workforce of a particular store.
   * If no employees are found for the given store ID, an IdNotFound exception is thrown.
   *
   * @param storeId The ID of the store to find employees for.
   *
   * @return {@link CustomApiResponse <List<EmployeeDTO>>} containing a list of employee DTOs and a success status.
   *
   * @throws IdNotFound       if no employees are found for the specified store ID.
   * @throws GeneralException if an error occurs during the search for employees.
   */
  public CustomApiResponse<List<EmployeeDTO>> getEmployeesFromStoreId(final Integer storeId) {
    try {
      List<Employee> employees = employeeRepository.getEmplotesByStoreId(storeId);
      List<EmployeeDTO> resultDTOs = employees.stream()
                                              .map(employeeMapper::employeeToEmployeeDTO)
                                              .collect(Collectors.toList());
      return new CustomApiResponse<>("Employees found successfully", resultDTOs);
    } catch (Exception e) {
      throw new GeneralException("Error finding employees for store ID: " + storeId + "; CAUSE: " + e.getMessage());
    }
  }



  /**
   * Finds all sales transactions associated with a specific store by its ID and converts them to DTOs.
   * This method allows for the analysis of sales performance for individual stores by retrieving a list of all sales
   * transactions
   * associated with a given store ID.
   *
   * @param storeId The ID of the store to find sales for.
   *
   * @return {@link CustomApiResponse <List<SaleDTO>>} containing a list of sale DTOs and a success status.
   *
   * @throws IdNotFound       if no sales are found for the specified store ID.
   * @throws GeneralException if an error occurs during the search for sales.
   */
  public CustomApiResponse<List<SaleDTO>> getSalesFromStoreId(final Integer storeId) {
    try {
      List<Sale> saleList = saleRepository.getByStoreId(storeId);
      if (saleList.isEmpty()) {
        throw new IdNotFound("No sales found for store ID: " + storeId);
      }
      List<SaleDTO> resultDTOs = saleList.stream().map(saleMapper::saleToSaleDTO).collect(Collectors.toList());
      return new CustomApiResponse<>("Sale found successfully", resultDTOs);
    } catch (IdNotFound e) {
      throw e;
    } catch (Exception e) {
      throw new GeneralException("Error finding sales: " + "CAUSE: " + e.getCause());
    }
  }


  /**
   * Calculates the total sales amount for a specific store by its ID.
   * This method aggregates the total sales made by a store, providing a sum of sales amounts,
   * which is useful for financial analysis and performance measurement of the store.
   *
   * @param storeId The ID of the store to calculate total sales for.
   *
   * @return {@link CustomApiResponse <Double>} containing the total sales amount and a success status.
   *
   * @throws GeneralException if an error occurs during the calculation of total sales.
   */
  public CustomApiResponse<Double> getTotalSalesByStore(final Integer storeId) {
    try {
      Double totalSales = saleRepository.getTotalByStoreId(storeId).orElse(0.0);
      return new CustomApiResponse<>("Total sales calculated successfully", totalSales);
    } catch (Exception error) {
      throw new GeneralException("Error calculating total sales: " + "CAUSE: " + error.getCause());
    }
  }


  /**
   * Retrieves a paginated and optionally filtered list of stores.
   * This method allows filtering by store ID, name, or location using specifications, and paginates the results.
   * It constructs a combined specification based on provided filter criteria, queries the repository,
   * and then maps the result to a DTO page.
   *
   * @param id       Optional. The unique identifier of the store. If specified, the results are filtered by this ID.
   * @param name     Optional. The name of the store. If specified, the results are filtered to include only stores
   *                 with this name.
   * @param location Optional. The location of the store. If specified, the results are filtered to include only
   *                 stores located in this area.
   * @param pageable A {@link Pageable} object specifying the page number and size for pagination.
   *
   * @return A {@link CustomApiResponse} containing a {@link Page} of {@link StoreDTO}, with a message indicating
   * successful retrieval of filtered stores.
   *
   * @throws GeneralException If there is any error during the querying process, encapsulating the underlying cause.
   */
  public CustomApiResponse<Page<StoreDTO>> getAllStoresPag(final Integer id, final String name, final String location,
                                                           final Pageable pageable) {
    try {
      StoreSpec specifications = new StoreSpec(id, name, location);
      Page<Store> storePage = storeRepository.findAll(specifications, pageable);
      Page<StoreDTO> resultDTOPage = storePage.map(storeMapper::storeToStoreDTO);
      return new CustomApiResponse<>("Filtered stores retrieved successfully", resultDTOPage);
    } catch (Exception error) {
      throw new GeneralException("Error finding filtered stores: " + "CAUSE: " + error.getCause());
    }
  }


  /**
   * Retrieves the top-selling stores and their sales figures.
   *
   * This method interacts with the store repository to obtain sales data and maps the results
   * into a list of {@link StoreDTO} objects. Each store is represented by a {@link StoreDTO}
   * containing the store's ID, name, and total sales. It encapsulates the results in a
   * {@link CustomApiResponse} with a success message.
   *
   * @return A {@link CustomApiResponse} that contains a list of {@link StoreDTO} objects,
   * each representing a store's sales data with status message indicating the operation outcome.
   */
  public CustomApiResponse<List<StoreDTO>> getStoreSales() {
    List<Object[]> results = storeRepository.findStoresTopSellers();
    List<StoreDTO> storeSales = new ArrayList<>();
    for (Object[] result : results) {
      StoreDTO dto = new StoreDTO();
      dto.setId(((Number) result[0]).intValue());
      dto.setName((String) result[1]);
      if (result.length > 2) {
        dto.setTotalSales(((Number) result[2]).doubleValue());
      }
      storeSales.add(dto);
    }
    return new CustomApiResponse<>("Success", storeSales);
  }

}
