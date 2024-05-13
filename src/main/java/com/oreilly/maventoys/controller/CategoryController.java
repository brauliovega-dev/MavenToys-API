package com.oreilly.maventoys.controller;

import com.oreilly.maventoys.model.DTO.CategoryDTO;
import com.oreilly.maventoys.model.DTO.ProductDTO;
import com.oreilly.maventoys.model.CustomApiResponse;
import com.oreilly.maventoys.service.CategoryService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for managing categories within the MavenToys application.
 * Provides endpoints for CRUD operations on categories, including listing all categories,
 * creating a new category, fetching a category by its ID, updating a category, and listing
 * products by category ID.
 */
@RestController
@RequestMapping(value = "/categories", produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "All endpoints related to categories.")
public class CategoryController {

  /**
   * Injected service for Category operations, facilitating interaction with the business logic layer.
   */
  private final CategoryService categoryService;


  /**
   * Retrieves all categories.
   *
   * @return A list of all CategoryDTOs wrapped in an ApiResponse.
   */
  @Operation(summary = "Retrieve a list of all active categories.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Get all categories",
          content = {
          @Content(mediaType = "application/json")}),
      @ApiResponse(responseCode = "400", description = "Bad Request / ID not " +
          "found", content = @Content)})
  @GetMapping
  public ResponseEntity<CustomApiResponse<List<CategoryDTO>>> getAllCategories() {
    return ResponseEntity.ok(categoryService.getAllCategories());
  }


  /**
   * Creates a new category.
   *
   * @param categoryDTO The category data transfer object containing the new category details.
   *
   * @return The created CategoryDTO wrapped in an ApiResponse, with HTTP status CREATED.
   */
  @Operation(summary = "Create a new category")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Category Created",
          content = {
          @Content(mediaType = "application/json")}),
      @ApiResponse(responseCode = "400", description = "Bad Request / ID not " +
          "found", content = @Content)})
  @PostMapping
  public ResponseEntity<CustomApiResponse<CategoryDTO>> createCategory(@RequestBody final CategoryDTO categoryDTO) {
    return ResponseEntity.ok(categoryService.createCategory(categoryDTO));
  }


  /**
   * Retrieves a category by its unique identifier.
   *
   * @param id The unique identifier of the category.
   *
   * @return The requested CategoryDTO wrapped in an ApiResponse.
   */
  @Operation(summary = "Get details of a specific category by its ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Category Created",
          content = {
          @Content(mediaType = "application/json")}),
      @ApiResponse(responseCode = "400", description = "Bad Request / ID not " +
          "found", content = @Content)})
  @GetMapping("/{id}")
  public ResponseEntity<CustomApiResponse<CategoryDTO>> getCategoryById(@PathVariable final Integer id) {
    return ResponseEntity.ok(categoryService.getCategoryById(id));
  }


  /**
   * Updates a category identified by its unique identifier with partial data.
   *
   * @param id          The unique identifier of the category to update.
   * @param categoryDTO The category data transfer object containing the updated category details.
   *
   * @return The updated CategoryDTO wrapped in an ApiResponse.
   */
  @Operation(summary = "Update category details partially by ID")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Category Updated.", content = {
      @Content(mediaType = "application/json")}),
                         @ApiResponse(responseCode = "400", description = "Bad Request / ID not found", content =
                         @Content)})
  @PatchMapping("/{id}")
  public ResponseEntity<CustomApiResponse<CategoryDTO>> patchCategory(@PathVariable final Integer id,
                                                                      @RequestBody final CategoryDTO categoryDTO) {
    return ResponseEntity.ok(categoryService.patchCategory(id, categoryDTO));
  }


  /**
   * Retrieves products associated with a specific category.
   *
   * @param id The unique identifier of the category whose products are to be retrieved.
   *
   * @return A list of ProductDTOs associated with the category, wrapped in an ApiResponse.
   */
  @Operation(summary = "Retrieve a list of products by Category ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Get employees by " +
          "StoreID", content = {
          @Content(mediaType = "application/json")}),
      @ApiResponse(responseCode = "400", description = "Bad Request / ID not " +
          "found", content = @Content)})
  @GetMapping("/{id}/products")
  public ResponseEntity<CustomApiResponse<List<ProductDTO>>> getProductsByCategoryId(@PathVariable final Integer id) {
    return ResponseEntity.ok(categoryService.findProductsByCategoryId(id));
  }



  /**
   * This method is responsible for fetching a paginated list of categories.
   * It is mapped to the '/paged' endpoint and uses the HTTP GET method.
   *
   * @param page  The page number to retrieve. If not provided, defaults to 0.
   * @param size  The number of records per page. If not provided, defaults to 10.
   * @param id    An optional parameter. If provided, the method will return categories with this id.
   * @param name  An optional parameter. If provided, the method will return categories with this name.
   *
   * @return ResponseEntity containing a paginated ApiResponse of CategoryDTOs.
   */
  @GetMapping("/paged")
  public ResponseEntity<CustomApiResponse<Page<CategoryDTO>>> getCategoriesPaged(
      @RequestParam(defaultValue = "0") final int page,
      @RequestParam(defaultValue = "10") final int size,
      @RequestParam(value = "id", required = false) final Integer id,
      @RequestParam(value = "name", required = false) final String name
  ) {
    Pageable pageable = PageRequest.of(page, size);
    CustomApiResponse<Page<CategoryDTO>> response = categoryService.getCategoriesPaged(pageable, id, name);
    return ResponseEntity.ok(response);
  }

  /**
   * Endpoint to fetch the total sales data by category.
   * This GET request is mapped to the '/sales' path and retrieves a list of CategoryDTO objects
   * which represent the sales data for each active category in the system.
   *
   * The response is encapsulated within a CustomApiResponse which includes potential metadata
   * for the response such as status codes, messages, etc., providing a standardized format
   * for front-end consumption.
   *
   * Usage is intended for clients requiring a report of sales by category where each category
   * is active and the total sales are calculated from associated active products.
   *
   * @return a CustomApiResponse containing a list of CategoryDTOs representing the sales data.
   */
  @GetMapping("/sales")
  public CustomApiResponse<List<CategoryDTO>> getCategorySales() {
    return categoryService.getCategorySales();
  }




}
