package com.oreilly.maventoys.service;

import com.oreilly.maventoys.model.DTO.EmployeeDTO;
import com.oreilly.maventoys.model.DTO.SaleDTO;
import com.oreilly.maventoys.exceptions.GeneralException;
import com.oreilly.maventoys.exceptions.IdNotFound;
import com.oreilly.maventoys.mapper.EmployeeMapper;
import com.oreilly.maventoys.mapper.SaleMapper;
import com.oreilly.maventoys.model.entity.Employee;
import com.oreilly.maventoys.model.entity.Sale;
import com.oreilly.maventoys.model.entity.Store;
import com.oreilly.maventoys.repository.EmployeeRepository;
import com.oreilly.maventoys.repository.StoreRepository;
import com.oreilly.maventoys.model.CustomApiResponse;
import com.oreilly.maventoys.repository.specifications.EmployeeSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Service class for handling employee-related operations within the application.
 * This includes the management of employee records such as creating, retrieving, updating,
 * and deleting employee data. It also includes operations to handle employee-related sales data.
 * The service uses EmployeeRepository for persistence operations and EmployeeMapper for
 * converting between entity and DTO representations. StoreRepository is also utilized for
 * validating and associating employees with stores.
 */
@Service
@RequiredArgsConstructor

public class EmployeeService {

  /**
   * Repository for managing employee data.
   */
  private final EmployeeRepository employeeRepository;

  /**
   * Repository for managing store data, used for associating employees with stores.
   */
  private final StoreRepository storeRepository;

  /**
   * The index in the result array that corresponds to the store ID of an employee.
   * This constant is used to access the store ID value from the array of objects
   * returned by the query in {@link #getTopSellers()} method.
   */
  private static final int STORE_ID_INDEX = 3;

  /**
   * The index in the result array that corresponds to the number of sales associated with an employee.
   * This constant is used to access the sales count value from the array of objects
   * returned by the query in {@link #getTopSellers()} method, ensuring that the code is free of magic numbers
   * and enhancing readability and maintainability.
   */
  private static final int NUMBER_OF_SALES_INDEX = 4;

  /**
   * Mapper for managing store data.
   */
  private final EmployeeMapper employeeMapper;

  /**
   * Mapper for managing Sale data.
   */
  private final SaleMapper saleMapper;

  /**
   * Fetches all employees currently marked as active within the database and converts their information into Data
   * Transfer Objects (DTOs).
   * This method is designed to retrieve a comprehensive list of all employees who are considered active, meaning
   * they are currently employed
   * and have not been marked as inactive or terminated. The conversion to DTOs facilitates easy data handling and
   * encapsulation, ensuring that
   * the information can be utilized efficiently in various parts of the application or external systems.
   *
   * @return A {@link List<EmployeeDTO>} containing the DTOs of all active employees. This list provides a
   * standardized format for employee data,
   * making it suitable for further processing, analysis, or API responses.
   *
   * @throws GeneralException if an unexpected error occurs during the process of fetching active employees from the
   *                          database. This exception
   *                          encapsulates any underlying issues, such as database access problems, to provide a
   *                          clear indication that the operation could not be completed as expected.
   */
  public CustomApiResponse<List<EmployeeDTO>> getAllEmployees() {
    try {
      List<Employee> activeEmployees = employeeRepository.getByActiveTrue();
      List<EmployeeDTO> employeeDTOs =
          activeEmployees.stream().map(employeeMapper::employeeToEmployeeDTO).collect(Collectors.toList());
      return new CustomApiResponse<>("Employees retrieved successfully", employeeDTOs);
    } catch (Exception error) {
      throw new GeneralException("Error fetching all active employees: " + "CAUSE: " + error.getCause());
    }
  }


  /**
   * Retrieves the details of a specific employee by their unique identifier (ID) and converts these details into a
   * Data Transfer Object (DTO).
   * This method serves to access the complete information of an employee, ensuring that if the employee exists in
   * the database,
   * their information is presented in an easily manageable format for further processing or response.
   *
   * @param id The unique identifier of the employee to retrieve.
   *
   * @return {@link CustomApiResponse <EmployeeDTO>} containing the details of the employee as a DTO and a success
   * status.
   *
   * @throws IdNotFound       if no employee with the specified ID is found, indicating the employee does not exist
   *                          in the database.
   * @throws GeneralException if an unforeseen problem occurs during the retrieval process, encapsulating any
   *                          underlying issue.
   */
  public CustomApiResponse<EmployeeDTO> getEmployeeById(final Integer id) {
    try {
      EmployeeDTO employeeDTO = employeeRepository.findById(id).map(employeeMapper::employeeToEmployeeDTO).orElseThrow(
          () -> new IdNotFound("Employee not found with ID: " + id));
      return new CustomApiResponse<>("Employee details fetched successfully", employeeDTO);
    } catch (IdNotFound error) {
      throw error;
    } catch (Exception error) {
      throw new GeneralException(
          "A problem was encountered while retrieving employees. " + "CAUSE: " + error.getCause());
    }
  }


  /**
   * Creates a new employee record in the database based on the data provided within an employee DTO.
   * This method is responsible for transforming the provided DTO into an employee entity, persisting it in the
   * database,
   * and then converting the newly created entity back into a DTO to be returned in the response.
   *
   * @param employeeDTO The DTO containing the data for creating a new employee.
   *
   * @return {@link CustomApiResponse <EmployeeDTO>} containing the details of the newly created employee as a DTO
   * and a success status.
   *
   * @throws GeneralException if an error is encountered during the process of creating the new employee record.
   */
  public CustomApiResponse<EmployeeDTO> createEmployee(final EmployeeDTO employeeDTO) {
    try {
      Employee employee = employeeMapper.employeeDTOToEmployee(employeeDTO);
      Employee savedEmployee = employeeRepository.save(employee);
      EmployeeDTO savedEmployeeDTO = employeeMapper.employeeToEmployeeDTO(savedEmployee);
      return new CustomApiResponse<>("Employee created successfully", savedEmployeeDTO);
    } catch (Exception error) {
      throw new GeneralException("Error creating employee: " + "CAUSE: " + error.getCause());
    }
  }


  /**
   * Partially updates an existing employee's information based on the provided DTO for a specified employee ID.
   * This method facilitates the selective update of employee fields without the need to provide a complete set of
   * employee information,
   * making it ideal for minor adjustments or updates to an employee's record.
   *
   * @param id          The unique identifier of the employee to update.
   * @param employeeDTO The DTO containing the data to be updated in the employee's record.
   *
   * @return {@link CustomApiResponse <EmployeeDTO>} containing the details of the updated employee as a DTO and a
   * success status.
   *
   * @throws IdNotFound       if no employee or related store by the given ID is found, indicating a reference
   *                          integrity issue.
   * @throws GeneralException if an error occurs during the process of updating the employee's information.
   */
  public CustomApiResponse<EmployeeDTO> patchEmployee(final Integer id, final EmployeeDTO employeeDTO) {
    try {
      EmployeeDTO updatedEmployeeDTO = employeeRepository.findById(id).map(employee -> {
        employeeMapper.updateEmployeeFromDto(employeeDTO, employee);

        // por si le asignamos un StoreID que no existe:
        if (employeeDTO.getStoreId() != null) {
          Store store = storeRepository.findById(employeeDTO.getStoreId()).orElseThrow(
              () -> new IdNotFound("Store not found for the given ID: " + employeeDTO.getStoreId()));
          employee.setStore(store);
        }

        Employee updatedEmployee = employeeRepository.save(employee);
        return employeeMapper.employeeToEmployeeDTO(updatedEmployee);
      }).orElseThrow(() -> new IdNotFound("Employee not found for the given ID: " + id));
      return new CustomApiResponse<>("Employee updated successfully", updatedEmployeeDTO);
    } catch (IdNotFound e) {
      throw e;
    } catch (Exception error) {
      throw new GeneralException("Error updating employee: " + "CAUSE: " + error.getCause());
    }
  }


  /**
   * Completely updates an employee's data based on the provided DTO for the specified employee ID.
   * This method overwrites the existing data of an employee with the new data contained within the provided DTO,
   * ensuring that the employee's record is fully aligned with the latest provided information.
   *
   * @param id          The unique identifier of the employee to update.
   * @param employeeDTO The DTO containing the new data for the employee.
   *
   * @return {@link CustomApiResponse <EmployeeDTO>} containing the updated details of the employee as a DTO and a
   * success status.
   *
   * @throws IdNotFound       if the employee with the specified ID is not found, indicating the employee does not
   *                          exist.
   * @throws GeneralException if an error is encountered during the update process of the employee.
   */
  public CustomApiResponse<EmployeeDTO> updateEmployee(final Integer id, final EmployeeDTO employeeDTO) {
    try {
      Employee employee =
          employeeRepository.findById(id).orElseThrow(() -> new IdNotFound("Employee not found with ID: " + id));
      Employee updatedEmployee = employeeMapper.updateEmployeeFromDto(employeeDTO, employee);
      employeeRepository.save(updatedEmployee);
      EmployeeDTO updatedEmployeeDTO = employeeMapper.employeeToEmployeeDTO(updatedEmployee);
      return new CustomApiResponse<>("Employee updated successfully", updatedEmployeeDTO);
    } catch (IdNotFound idNotFound) {
      throw idNotFound;
    } catch (Exception error) {
      throw new GeneralException("Error updating employee: " + "CAUSE: " + error.getCause());
    }
  }


  /**
   * Retrieves sales data associated with a specific employee by their ID, converting each sale to a DTO.
   * This method aims to gather and present all sales transactions made by or attributed to an employee,
   * providing insight into their sales performance or activities.
   *
   * @param employeeId The unique identifier of the employee whose sales data is to be retrieved.
   *
   * @return {@link CustomApiResponse <List<SaleDTO>>} containing a list of sales DTOs associated with the employee
   * and a success status.
   *
   * @throws IdNotFound       if no sales are found for the specified employee ID, suggesting the employee has not
   *                          made any sales.
   * @throws GeneralException if an error occurs during the process of fetching the sales data.
   */
  public CustomApiResponse<List<SaleDTO>> getSalesByEmployee(final Integer employeeId) {
    try {
      List<Sale> sales = employeeRepository.getAllSalesByEmployeeId(employeeId);
      if (sales.isEmpty()) {
        throw new IdNotFound("No sales found for employee ID " + employeeId);
      } //TODO quitar if si quiero
      List<SaleDTO> salesDTOs = sales.stream().map(saleMapper::saleToSaleDTO).collect(Collectors.toList());
      return new CustomApiResponse<>("Sale fetched successfully", salesDTOs);
    } catch (IdNotFound e) {
      throw e;
    } catch (Exception error) {
      throw new GeneralException("Error fetching sales: " + "CAUSE: " + error.getCause());
    }
  }


  /**
   * Retrieves a paginated list of employees based on the provided search criteria.
   * This method allows for the retrieval of a subset of employees based on specific search criteria,
   * such as employee ID, first name, or last name. The results are paginated to improve performance
   * and reduce the amount of data transferred in each request.
   *
   * @param pageable  The pagination information, including page number, size, and sort order.
   * @param id        The unique identifier of the employee to search for.
   * @param firstName The first name of the employee to search for.
   * @param lastName  The last name of the employee to search for.
   *
   * @return {@link CustomApiResponse <Page<EmployeeDTO>>} containing a paginated list of employee DTOs
   * based on the search criteria and a success status.
   *
   * @throws GeneralException if an error occurs during the process of fetching the employees based on the search
   *                          criteria.
   */
  public CustomApiResponse<Page<EmployeeDTO>> getAllEmployeesPaged(final Pageable pageable, final Integer id,
                                                                   final String firstName, final String lastName) {
    try {
      EmployeeSpec specifications = new EmployeeSpec(id, firstName, lastName);
      Page<Employee> activeEmployees = employeeRepository.findAll(specifications, pageable);
      Page<EmployeeDTO> activeEmployeesDTOs = activeEmployees.map(employeeMapper::employeeToEmployeeDTO);
      return new CustomApiResponse<>("Active employees fetched successfully", activeEmployeesDTOs);
    } catch (Exception error) {
      throw new GeneralException("Error fetching active employees: " + "CAUSE: " + error.getCause());
    }
  }

  /**
   * Retrieves a list of the top-selling employees from the repository,
   * each with their sales figures.
   * <p>
   * This method executes a query through {@code employeeRepository} to fetch the top sellers
   * based on sales. Each result is then transformed into an {@link EmployeeDTO} object, encapsulating
   * the employee's ID, first name, last name, store ID, and number of sales. This conversion ensures
   * that only relevant information is passed on, enhancing data privacy and minimizing bandwidth usage.
   * </p>
   * <p>
   * It also includes a safeguard to prevent index out of bounds exceptions by checking the result array's length
   * before attempting to access the number of sales, ensuring robustness in handling incomplete data.
   * </p>
   *
   * @return a {@link List} of {@link EmployeeDTO} objects representing the top-selling employees,
   * each with their associated sales count. The list is intended for use in reporting and
   * analytics contexts, providing insights into employee performance.
   */
  public List<EmployeeDTO> getTopSellers() {
    List<Object[]> results = employeeRepository.findTopSellersWithStoreId();
    List<EmployeeDTO> topSellers = new ArrayList<>();
    for (Object[] result : results) {
      EmployeeDTO dto = new EmployeeDTO();
      dto.setId(((Number) result[0]).intValue()); // ID del empleado
      dto.setFirstName((String) result[1]); // Nombre del empleado
      dto.setLastName((String) result[2]); // Apellido del empleado
      dto.setStoreId(((Number) result[STORE_ID_INDEX]).intValue()); // ID de la tienda usando constante
      if (result.length > NUMBER_OF_SALES_INDEX) { // Verificación para evitar errores de índice usando constante
        dto.setNumberOfSales(((Number) result[NUMBER_OF_SALES_INDEX]).longValue()); // Número de ventas usando constante
      }
      topSellers.add(dto);
    }
    return topSellers;
  }
}
