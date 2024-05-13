package com.oreilly.maventoys.controller;


import com.oreilly.maventoys.model.DTO.SaleDTO;
import com.oreilly.maventoys.model.CustomApiResponse;
import com.oreilly.maventoys.service.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller for managing sales transactions within the MavenToys application.
 * Provides endpoints for CRUD operations on sales, as well as querying sales by date range.
 */
@RestController
@RequestMapping(value = "/sales", produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Sale", description = "All endpoints related to sales.")

public class SaleController {

  /**
   * Injected service for Sale operations, facilitating interaction with the business logic layer.
   */
  private final SaleService saleService;


  /**
   * Retrieves a paginated list of all sales transactions.
   *
   * @param pageable Pagination details.
   *
   * @return ResponseEntity containing a paginated ApiResponse of SaleDTOs.
   */
  @Operation(summary = "Retrieve a list of all active sales")
  @ApiResponses(value = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Get all sales",
          content = {
          @Content(mediaType = "application/json")}),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request / ID not " +
          "found", content = @Content)})
  @GetMapping()
  public ResponseEntity<CustomApiResponse<Page<SaleDTO>>> getAllSales(final Pageable pageable) {
    return ResponseEntity.ok(saleService.getAllSales(pageable));
  }


  /**
   * Fetches details of a specific sale by its unique identifier.
   *
   * @param id The ID of the sale to retrieve.
   *
   * @return ResponseEntity with the sale's details wrapped in an ApiResponse.
   */
  @Operation(summary = "Retrieve a sale by its ID")
  @ApiResponses(value = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Get sale by its ID.",
          content = {
          @Content(mediaType = "application/json")}),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request / ID not " +
          "found", content = @Content)})
  @GetMapping("/{id}")
  public ResponseEntity<CustomApiResponse<SaleDTO>> getSaleById(@PathVariable final Integer id) {
    return ResponseEntity.ok(saleService.getSalesById(id));
  }


  /**
   * Creates a new sales transaction with the provided sale details.
   *
   * @param saleDTO DTO containing the new sale's details.
   *
   * @return ResponseEntity with the created sale's details wrapped in an ApiResponse, with HTTP status CREATED.
   */
  @Operation(summary = "Create a new sale")
  @ApiResponses(value = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Sale created.",
          content = {
          @Content(mediaType = "application/json")}),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request / ID not " +
          "found", content = @Content)})
  @PostMapping
  public ResponseEntity<CustomApiResponse<SaleDTO>> createSale(@RequestBody final SaleDTO saleDTO) {
    return ResponseEntity.status(HttpStatus.CREATED).body(saleService.createSale(saleDTO));
  }


  /**
   * Updates details of an existing sale identified by its ID.
   *
   * @param id      The ID of the sale to update.
   * @param saleDTO DTO containing updated sale details.
   *
   * @return ResponseEntity with the updated sale's details wrapped in an ApiResponse.
   */
  @Operation(summary = "Update sale details partially by ID")
  @ApiResponses(value = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Sale Updated.",
          content = {
          @Content(mediaType = "application/json")}),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request / ID not " +
          "found", content = @Content)})
  @PatchMapping("/{id}")
  public ResponseEntity<CustomApiResponse<SaleDTO>> updateSale(@PathVariable final Integer id,
                                                               @RequestBody final SaleDTO saleDTO) {
    return ResponseEntity.ok(saleService.updateSale(id, saleDTO));
  }

  /**
   * Retrieves sales transactions within a specified date range.
   *
   * @param startDate The start date of the range.
   * @param endDate   The end date of the range.
   *
   * @return ResponseEntity containing an ApiResponse with a list of SaleDTOs.
   */
  @Operation(summary = "Get sales by date")
  @ApiResponses(value = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = ".", content = {
          @Content(mediaType = "application/json")}),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request / ID not " +
          "found", content = @Content)})
  @GetMapping("/byDateRange")
  public ResponseEntity<CustomApiResponse<List<SaleDTO>>> getSalesByDateRange(
      @RequestParam("startDate") final LocalDate startDate, @RequestParam("endDate") final LocalDate endDate) {
    return ResponseEntity.ok(saleService.getSalesByDate(startDate, endDate));
  }


  /**
   * Retrieves a paginated list of all sales transactions.
   *
   * @param page The page number to retrieve.
   * @param size The number of items per page.
   * @param id The ID of the sale to retrieve.
   * @param storeId The ID of the store to retrieve.
   * @param employeeId The ID of the employee to retrieve.
   *
   * @return ResponseEntity containing a paginated ApiResponse of SaleDTOs.
   */
  @GetMapping("/paged")
  public ResponseEntity<CustomApiResponse<Page<SaleDTO>>> getSalesPaged(
      @RequestParam(defaultValue = "0") final int page, @RequestParam(defaultValue = "10") final int size,
      @RequestParam(value = "id", required = false) final Integer id,
      @RequestParam(value = "storeId", required = false) final Integer storeId,
      @RequestParam(value = "employeeId", required = false) final Integer employeeId) {
    Pageable pageable = PageRequest.of(page, size);
    CustomApiResponse<Page<SaleDTO>> response = saleService.getAllSalesPaged(pageable, id, storeId, employeeId);
    return ResponseEntity.ok(response);
  }

}
