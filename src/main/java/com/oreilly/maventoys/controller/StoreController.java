package com.oreilly.maventoys.controller;

import com.oreilly.maventoys.model.DTO.EmployeeDTO;
import com.oreilly.maventoys.model.DTO.SaleDTO;
import com.oreilly.maventoys.model.DTO.StoreDTO;
import com.oreilly.maventoys.model.CustomApiResponse;
import com.oreilly.maventoys.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
 * Rest controller for handling operations related to stores. This controller
 * provides endpoints for retrieving, creating, updating stores, and querying
 * store-related data such as employees and sales.
 */
@RestController
@RequestMapping(value = "/stores", produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Stores", description = "All endpoints related to stores.")

public class StoreController {

  /**
   * Injected service for Stores operations, facilitating interaction with the business logic layer.
   */
  private final StoreService storeService;


  /**
   * Retrieve all active stores.
   *
   * @return ResponseEntity with ApiResponse containing a list of StoreDTOs.
   */
  @Operation(summary = "Retrieve a list of all active stores")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Get all stores",
          content = {
          @Content(mediaType = "application/json")}),
      @ApiResponse(responseCode = "400", description = "Bad Request / ID not " +
          "found", content = @Content)})
  @GetMapping()
  public ResponseEntity<CustomApiResponse<List<StoreDTO>>> getActiveStores() {
    CustomApiResponse<List<StoreDTO>> response = storeService.getStores();
    return ResponseEntity.ok(response);
  }


  /**
   * Get details of a specific store by its ID.
   *
   * @param id Store's unique identifier.
   *
   * @return ResponseEntity with ApiResponse containing StoreDTO.
   */
  @Operation(summary = "Get details of a specific store by its ID")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Found the store"),
                         @ApiResponse(responseCode = "400", description = "Invalid ID supplied", content =
                         @Content(schema = @Schema(implementation = CustomApiResponse.class))),
                         @ApiResponse(responseCode = "404", description = "Store not found", content =
                         @Content(schema = @Schema(implementation = CustomApiResponse.class)))})
  @GetMapping("/{id}")
  public ResponseEntity<CustomApiResponse<StoreDTO>> getStoreById(final @PathVariable Integer id) {
    CustomApiResponse<StoreDTO> response = storeService.getStoreById(id);
    return ResponseEntity.ok(response);
  }


  /**
   * Create a new store with provided store details.
   *
   * @param storeDTO DTO containing the new store's details.
   *
   * @return ResponseEntity with ApiResponse containing the created StoreDTO.
   */
  @Operation(summary = "Create a new store")
  @ApiResponses(value = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Store created.",
          content = {
          @Content(mediaType = "application/json")}),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request / ID not " +
          "found", content = @Content)})
  @PostMapping
  public ResponseEntity<CustomApiResponse<StoreDTO>> createStore(final @RequestBody StoreDTO storeDTO) {
    CustomApiResponse<StoreDTO> response = storeService.createStore(storeDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  /**
   * Partially update store details by ID.
   *
   * @param id       Store's unique identifier.
   * @param storeDTO DTO containing updated store details.
   *
   * @return ResponseEntity with ApiResponse containing the updated StoreDTO.
   */
  @Operation(summary = "Update store details partially by ID")
  @ApiResponses(value = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Store Updated.",
          content = {
          @Content(mediaType = "application/json")}),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request / ID not " +
          "found", content = @Content)})
  @PatchMapping("/{id}")
  public ResponseEntity<CustomApiResponse<StoreDTO>> patchStore(final @PathVariable int id,
                                                                final @RequestBody StoreDTO storeDTO) {
    CustomApiResponse<StoreDTO> response = storeService.patchStore(id, storeDTO);
    return ResponseEntity.ok(response);
  }


  /**
   * Fully update store details by ID.
   *
   * @param id       Store's unique identifier.
   * @param storeDTO DTO containing updated store details.
   *
   * @return ResponseEntity with ApiResponse containing the updated StoreDTO.
   */
  @Operation(summary = "Update store details completely by ID")
  @ApiResponses(value = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Updated Store.",
          content = {
          @Content(mediaType = "application/json")}),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request / ID not " +
          "found", content = @Content)})
  @PutMapping("/{id}")
  public ResponseEntity<CustomApiResponse<StoreDTO>> updateStore(final @PathVariable Integer id,
                                                                 final @RequestBody StoreDTO storeDTO) {
    CustomApiResponse<StoreDTO> response = storeService.updateStore(id, storeDTO);
    return ResponseEntity.ok(response);
  }


  /**
   * Retrieve a list of employees by Store ID.
   *
   * @param id Store's unique identifier.
   *
   * @return ResponseEntity with ApiResponse containing a list of EmployeeDTOs.
   */
  @Operation(summary = "Retrieve a list of employees by Store ID")
  @ApiResponses(value = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Get employees by " +
          "StoreID", content = {@Content(mediaType = "application/json")}),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request / ID not " +
          "found", content = @Content)})

  @GetMapping("/{id}/employees")
  public ResponseEntity<CustomApiResponse<List<EmployeeDTO>>> getEmployeesByStoreId(final @PathVariable Integer id) {
    CustomApiResponse<List<EmployeeDTO>> response = storeService.getEmployeesFromStoreId(id);
    return ResponseEntity.ok(response);
  }


  /**
   * Get all sales associated with a specific Store ID.
   *
   * @param id Store's unique identifier.
   *
   * @return ResponseEntity with ApiResponse containing a list of SaleDTOs.
   */
  @Operation(summary = "Get all sales associated with a specific Store ID")
  @ApiResponses(value = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Get all sales by " +
          "Store ID", content = {@Content(mediaType = "application/json")}),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request / ID not " +
          "found", content = @Content)})
  @GetMapping("/{id}/sales")

  public ResponseEntity<CustomApiResponse<List<SaleDTO>>> getSalesByStoreId(final @PathVariable int id) {
    CustomApiResponse<List<SaleDTO>> response = storeService.getSalesFromStoreId(id);
    return ResponseEntity.ok(response);
  }


  /**
   * Retrieve the total sales amount by Store ID.
   *
   * @param id Store's unique identifier.
   *
   * @return ResponseEntity with ApiResponse containing the total sales amount.
   */
  @Operation(summary = "Retrieve the total sales amount by Store ID")
  @ApiResponses(value = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Get all sales by " +
          "Store ID", content = {@Content(mediaType = "application/json")}),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request / ID not " +
          "found", content = @Content)})
  @GetMapping("/{id}/totalSales")
  public ResponseEntity<CustomApiResponse<Double>> getTotalSalesByStore(final @PathVariable Integer id) {
    CustomApiResponse<Double> response = storeService.getTotalSalesByStore(id);
    return ResponseEntity.ok(response);
  }


  /**
   * Retrieves a paginated list of stores based on optional filter parameters.
   * This method supports pagination and filtering by store ID, name, or location.
   *
   * @param page     The page number to retrieve, with a default value of 0 if not specified.
   * @param size     The number of stores to display per page, with a default value of 10 if not specified.
   * @param id       Optional. The unique identifier of the store. If specified, the method filters the result to
   *                 include only the store with this ID.
   * @param name     Optional. The name of the store. If specified, the method filters the result to include only
   *                 stores with this name.
   * @param location Optional. The location of the store. If specified, the method filters the result to include only
   *                stores in this location.
   *
   * @return ResponseEntity containing a {@link CustomApiResponse} with a {@link Page} of {@link StoreDTO},
   * representing the paginated list of stores. The response is always successful with an HTTP OK status.
   */

  @Operation(summary = "Get a paginated list of stores")
  @ApiResponses(value = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Get all stores by " +
          "page.", content = {@Content(mediaType = "application/json")}),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request / ID not " +
          "found", content = @Content)})
  @GetMapping("/paged")
  public ResponseEntity<CustomApiResponse<Page<StoreDTO>>> getStoresPaged(
      final @RequestParam(defaultValue = "0") int page, final @RequestParam(defaultValue = "10") int size,
      final @RequestParam(required = false) Integer id, final @RequestParam(required = false) String name,
      final @RequestParam(required = false) String location) {

    Pageable pageable = PageRequest.of(page, size);
    CustomApiResponse<Page<StoreDTO>> response = storeService.getAllStoresPag(id, name, location, pageable);
    return ResponseEntity.ok(response);
  }


  /**
   * Handles the HTTP GET request for sales data.
   *
   * This endpoint provides a list of sales categorized by store. It retrieves store-related sales information
   * and wraps the data within a custom API response structure.
   *
   * @return A {@link CustomApiResponse} object that contains a list of {@link StoreDTO} objects, each representing a
   * store's sales data.
   */
  @GetMapping("/sales")
  public CustomApiResponse<List<StoreDTO>> getStoreSales() {
    return storeService.getStoreSales();
  }


}
