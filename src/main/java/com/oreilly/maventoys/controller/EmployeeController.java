package com.oreilly.maventoys.controller;

import com.oreilly.maventoys.model.DTO.EmployeeDTO;
import com.oreilly.maventoys.model.DTO.SaleDTO;
import com.oreilly.maventoys.model.CustomApiResponse;
import com.oreilly.maventoys.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * Controller for managing employee-related operations within the MavenToys application.
 * Facilitates CRUD operations for employees and provides endpoints for listing employees,
 * creating, fetching, updating employee details, and retrieving employee sales.
 */
@RestController
@RequestMapping(value = "/employees")
@RequiredArgsConstructor

@Tag(name = "Employees", description = "All endpoints related to employees.")

public class EmployeeController {

  /**
   * Injected service for employee operations, facilitating interaction with the business logic layer.
   */
  private final EmployeeService employeeService;


  /**
   * Retrieves a list of all active employees.
   *
   * @return ResponseEntity containing an ApiResponse with a list of EmployeeDTOs.
   */
  @Operation(summary = "Retrieve a list of all active employees")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Get all employees", content = {
      @Content(mediaType = "application/json")}),
                         @ApiResponse(responseCode = "400", description = "Bad Request / ID not " +
                             "found", content = @Content)})
  @GetMapping
  public ResponseEntity<CustomApiResponse<List<EmployeeDTO>>> getActiveEmployees() {
    CustomApiResponse<List<EmployeeDTO>> response = employeeService.getAllEmployees();
    return ResponseEntity.ok(response);
  }

  /**
   * Fetches details of an employee by their ID.
   *
   * @param id The ID of the employee to retrieve.
   *
   * @return ResponseEntity with the employee's details wrapped in an ApiResponse.
   */
  @Operation(summary = "Retrieve a employee by its ID")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Get employee.", content = {
      @Content(mediaType = "application/json")}),
                         @ApiResponse(responseCode = "400", description = "Bad Request / ID not " +
                             "found", content = @Content)})
  @GetMapping("/{id}")
  public ResponseEntity<CustomApiResponse<EmployeeDTO>> getEmployeeById(@PathVariable final Integer id) {
    CustomApiResponse<EmployeeDTO> response = employeeService.getEmployeeById(id);
    return ResponseEntity.ok(response);
  }


  /**
   * Creates a new employee with the provided employee details.
   *
   * @param employeeDTO DTO containing the new employee's details.
   *
   * @return ResponseEntity with the created employee's details wrapped in an ApiResponse.
   */
  @Operation(summary = "Create a new employee")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Employee created.", content = {
      @Content(mediaType = "application/json")}),
                         @ApiResponse(responseCode = "400", description = "Bad Request / ID not " +
                             "found", content = @Content)})
  @PostMapping
  public ResponseEntity<CustomApiResponse<EmployeeDTO>> createEmployee(@RequestBody final EmployeeDTO employeeDTO) {
    CustomApiResponse<EmployeeDTO> response = employeeService.createEmployee(employeeDTO);
    return ResponseEntity.ok(response);
  }


  /**
   * Updates details of an existing employee identified by their ID with partial data.
   *
   * @param id          The ID of the employee to update.
   * @param employeeDTO DTO containing updated employee details.
   *
   * @return ResponseEntity with the updated employee's details wrapped in an ApiResponse.
   */
  @Operation(summary = "Update employee details partially by ID")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Store Updated.", content = {
      @Content(mediaType = "application/json")}),
                         @ApiResponse(responseCode = "400", description = "Bad Request / ID not " +
                             "found", content = @Content)})
  @PatchMapping("/{id}")
  public ResponseEntity<CustomApiResponse<EmployeeDTO>> patchEmployee(@PathVariable final Integer id,
                                                                      @RequestBody final EmployeeDTO employeeDTO) {
    CustomApiResponse<EmployeeDTO> response = employeeService.patchEmployee(id, employeeDTO);
    return ResponseEntity.ok(response);
  }


  /**
   * Fully updates an existing employee identified by their ID.
   *
   * @param id          The ID of the employee to update.
   * @param employeeDTO DTO containing new employee details.
   *
   * @return ResponseEntity with the updated employee details wrapped in an ApiResponse.
   */
  @Operation(summary = "Update employees details completely by ID")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Updated Store.", content = {
      @Content(mediaType = "application/json")}),
                         @ApiResponse(responseCode = "400", description = "Bad Request / ID not " +
                             "found", content = @Content)})
  @PutMapping("/{id}")
  public ResponseEntity<CustomApiResponse<EmployeeDTO>> updateEmployee(@PathVariable final Integer id,
                                                                       @RequestBody final EmployeeDTO employeeDTO) {
    CustomApiResponse<EmployeeDTO> response = employeeService.updateEmployee(id, employeeDTO);
    return ResponseEntity.ok(response);
  }


  /**
   * Retrieves sales records associated with a specific employee by their ID.
   *
   * @param id The ID of the employee whose sales records are to be retrieved.
   *
   * @return ResponseEntity containing an ApiResponse with a list of SaleDTOs.
   */
  @Operation(summary = "Retrieve a list of sales by Employee ID")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Get sales by " + "EmployeeID", content = {
      @Content(mediaType = "application/json")}),
                         @ApiResponse(responseCode = "400", description = "Bad Request / ID not " +
                             "found", content = @Content)})
  @GetMapping("/{id}/sales")
  public ResponseEntity<CustomApiResponse<List<SaleDTO>>> getSalesByEmployeeId(@PathVariable final Integer id) {
    CustomApiResponse<List<SaleDTO>> response = employeeService.getSalesByEmployee(id);
    return ResponseEntity.ok(response);
  }


  /**
   * Retrieves a paginated list of active employees.
   * <p>
   * This endpoint supports optional filtering by employee ID, first name, or last name to allow
   * for more refined searches. Pagination parameters can also be adjusted with the "page" and "limit" request
   * parameters.
   * </p>
   *
   * @param page      the page number to retrieve, with a default value of 0 if not specified (0-indexed).
   * @param limit     the maximum number of entries per page, with a default of 10 if not specified.
   * @param id        an optional parameter to filter the search by employee ID. If not provided, all employee
   *                  records are considered.
   * @param firstName an optional parameter to filter by employees' first names. If not provided, this filter is not
   *                  applied.
   * @param lastName  an optional parameter to filter by employees' last names. If not provided, this filter is not
   *                  applied.
   *
   * @return a {@link ResponseEntity} containing a {@link CustomApiResponse} with a {@link Page} of {@link EmployeeDTO},
   * indicating the current slice of data depending on the pagination and any applied filters. The response is
   * wrapped in a
   * successful HTTP response with a 200 status code when the request is properly handled. Returns a 400 status error
   * if there
   * is a bad request such as an invalid ID.
   *
   * @apiNote This method uses GET request under the path "/paged".
   * @implNote This method handles the pagination and filtering logic by delegating to the {@code employeeService}.
   */
  @Operation(summary = "Get a paginated list of employees")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Get all employees by " + "page.", content = {
      @Content(mediaType = "application/json")}),
                         @ApiResponse(responseCode = "400", description = "Bad Request / ID not " +
                             "found", content = @Content)})
  @GetMapping("/paged")
  public ResponseEntity<CustomApiResponse<Page<EmployeeDTO>>> getActiveEmployeesPaged(
      @RequestParam(value = "page", defaultValue = "0") final int page,
      @RequestParam(value = "limit", defaultValue = "10") final int limit,
      @RequestParam(value = "id", required = false) final Integer id,
      @RequestParam(value = "firstName", required = false) final String firstName,
      @RequestParam(value = "lastName", required = false) final String lastName) {
    Pageable pageable = PageRequest.of(page, limit);
    CustomApiResponse<Page<EmployeeDTO>> response =
        employeeService.getAllEmployeesPaged(pageable, id, firstName, lastName);
    return ResponseEntity.ok(response);
  }


  /**
   * Handles the HTTP GET request for retrieving the list of top-selling employees.
   * <p>
   * This method calls the {@code employeeService} to obtain a list of {@link EmployeeDTO} objects
   * representing the employees with the highest sales. It then wraps this list in a {@link ResponseEntity}
   * with an HTTP status code of 200 (OK), indicating successful retrieval of the data.
   * </p>
   *
   * @return a {@link ResponseEntity} containing a list of {@link EmployeeDTO}s representing the top sellers,
   * along with an HTTP 200 (OK) status code. Each {@link EmployeeDTO} includes employee details
   * and their sales performance metrics.
   */
  @GetMapping("/top-sellers")
  public ResponseEntity<List<EmployeeDTO>> getTopSellers() {
    List<EmployeeDTO> topSellers = (List<EmployeeDTO>) employeeService.getTopSellers();
    return ResponseEntity.ok(topSellers);
  }

}
