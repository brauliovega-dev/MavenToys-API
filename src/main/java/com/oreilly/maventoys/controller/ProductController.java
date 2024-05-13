package com.oreilly.maventoys.controller;

import com.oreilly.maventoys.model.DTO.ProductDTO;
import com.oreilly.maventoys.model.DTO.SaleDTO;
import com.oreilly.maventoys.model.CustomApiResponse;
import com.oreilly.maventoys.model.DTO.StockResponse;
import com.oreilly.maventoys.service.ProductService;
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
 * Controller for managing product-related operations within the MavenToys application.
 * Offers CRUD functionality for products, alongside specialized endpoints for inventory stock, sales data,
 * and price history for each product.
 */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "All endpoints related to products.")
public class ProductController {

  /**
   * Injected service for product operations, facilitating interaction with the business logic layer.
   */
  private final ProductService productService;


  /**
   * Retrieves a list of all active products.
   *
   * @return ResponseEntity containing an ApiResponse with a list of ProductDTOs.
   */
  @Operation(summary = "Retrieve a list of all active products")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Get all products", content = {
      @Content(mediaType = "application/json")}),
                         @ApiResponse(responseCode = "400", description = "Bad Request / ID not " +
                             "found", content = @Content)})
  @GetMapping
  public ResponseEntity<CustomApiResponse<List<ProductDTO>>> getActiveProducts() {
    CustomApiResponse<List<ProductDTO>> response = productService.getProducts();
    return ResponseEntity.ok(response);
  }

  /**
   * Fetches details of a product by its unique identifier.
   *
   * @param id The ID of the product to retrieve.
   *
   * @return ResponseEntity with the product's details wrapped in an ApiResponse.
   */
  @Operation(summary = "Get details of a specific store by its ID")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Get product by ID", content = {
      @Content(mediaType = "application/json")}),
                         @ApiResponse(responseCode = "400", description = "Bad Request / ID not " +
                             "found", content = @Content)})
  @GetMapping("/{id}")
  public ResponseEntity<CustomApiResponse<ProductDTO>> getProductById(@PathVariable final Integer id) {
    CustomApiResponse<ProductDTO> response = productService.getProductById(id);
    return ResponseEntity.ok(response);
  }


  /**
   * Creates a new product with initial inventory stock.
   *
   * @param productDTO DTO containing the new product's details.
   *
   * @return ResponseEntity with the created product's details wrapped in an ApiResponse, with HTTP status CREATED.
   */
  @Operation(summary = "Create a new product.")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Product created.", content = {
      @Content(mediaType = "application/json")}),
                         @ApiResponse(responseCode = "400", description = "Bad Request / ID not " +
                             "found", content = @Content)})
  @PostMapping
  public ResponseEntity<CustomApiResponse<ProductDTO>> createProduct(@RequestBody final ProductDTO productDTO) {
    CustomApiResponse<ProductDTO> response = productService.createProduct(productDTO, 1);
    return ResponseEntity.ok(response);
  }


  /**
   * Updates details of an existing product identified by its ID with partial data.
   *
   * @param id         The ID of the product to update.
   * @param productDTO DTO containing updated product details.
   *
   * @return ResponseEntity with the updated product's details wrapped in an ApiResponse.
   */
  @Operation(summary = "Update products details partially by ID")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Product Updated.", content = {
      @Content(mediaType = "application/json")}),
                         @ApiResponse(responseCode = "400", description = "Bad Request / ID not " +
                             "found", content = @Content)})
  @PatchMapping("/{id}")
  public ResponseEntity<CustomApiResponse<ProductDTO>> patchProduct(@PathVariable final int id,
                                                                    @RequestBody final ProductDTO productDTO) {
    CustomApiResponse<ProductDTO> response = productService.patchProduct(id, productDTO);
    return ResponseEntity.ok(response);
  }


  /**
   * Fully updates an existing product identified by its ID.
   *
   * @param id         The ID of the product to update.
   * @param productDTO DTO containing new product details.
   *
   * @return ResponseEntity with the updated product details wrapped in an ApiResponse.
   */
  @Operation(summary = "Update products details completely by ID")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Updated Product.", content = {
      @Content(mediaType = "application/json")}),
                         @ApiResponse(responseCode = "400", description = "Bad Request / ID not " +
                             "found", content = @Content)})
  @PutMapping("/{id}")
  public ResponseEntity<CustomApiResponse<ProductDTO>> updateProduct(@PathVariable final Integer id,
                                                                     @RequestBody final ProductDTO productDTO) {
    CustomApiResponse<ProductDTO> response = productService.updateProduct(id, productDTO);
    return ResponseEntity.ok(response);
  }


  /**
   * Retrieves the stock level for a specific product by its ID.
   *
   * @param id The ID of the product whose stock level is queried.
   *
   * @return ResponseEntity containing an ApiResponse with stock details.
   */
  @Operation(summary = "Get stock by product ID")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Product Stock.", content = {
      @Content(mediaType = "application/json")}),
                         @ApiResponse(responseCode = "400", description = "Bad Request / ID not " +
                             "found", content = @Content)})
  @GetMapping("/{id}/stock")
  public ResponseEntity<CustomApiResponse<StockResponse>> getStockByProductId(@PathVariable final Integer id) {
    CustomApiResponse<StockResponse> response = productService.getProductInventory(id);
    return ResponseEntity.ok(response);
  }


  /**
   * Retrieves sales data associated with a specific product by its ID.
   *
   * @param id The ID of the product whose sales data is queried.
   *
   * @return ResponseEntity containing an ApiResponse with a list of SaleDTOs.
   */
  @Operation(summary = "Retrieve a list of sales by Product ID")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Get sales by " + "ProductID", content = {
      @Content(mediaType = "application/json")}),
                         @ApiResponse(responseCode = "400", description = "Bad Request / ID not " +
                             "found", content = @Content)})
  @GetMapping("/{id}/sales")
  public ResponseEntity<CustomApiResponse<List<SaleDTO>>> getSalesByProductId(@PathVariable final Integer id) {
    CustomApiResponse<List<SaleDTO>> response = productService.getSalesByProductId(id);
    return ResponseEntity.ok(response);
  }


  /**
   * Retrieves the price history for a specific product by its ID.
   *
   * @param id The ID of the product whose price history is queried.
   *
   * @return ResponseEntity containing an ApiResponse with a list of ProductDTOs reflecting price changes.
   */
  @Operation(summary = "Retrieve a list of prices by Product ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Get price History by " + "ProductID", content = {
          @Content(mediaType = "application/json")}),
      @ApiResponse(responseCode = "400", description = "Bad Request / ID not " + "found", content = @Content)})
  @GetMapping("/{id}/price-history")
  public ResponseEntity<CustomApiResponse<List<ProductDTO>>> getPriceHistory(@PathVariable("id") final Integer id) {
    CustomApiResponse<List<ProductDTO>> response = productService.getPriceHistoryByProductId(id);
    return ResponseEntity.ok(response);
  }


  /**
   * This method is responsible for fetching a paginated list of products.
   * It is mapped to the '/paged' endpoint and uses the HTTP GET method.
   *
   * @param page  The page number to retrieve. If not provided, defaults to 0.
   * @param limit The number of records per page. If not provided, defaults to 10.
   * @param id    An optional parameter. If provided, the method will return products with this id.
   * @param name  An optional parameter. If provided, the method will return products with this name.
   *
   * @return ResponseEntity containing a paginated ApiResponse of ProductDTOs.
   *
   * @Operation This annotation provides a brief summary for the Swagger UI documentation.
   * @ApiResponses This annotation provides possible responses for the Swagger UI documentation.
   */
  @Operation(summary = "Get a paginated list of Products")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Get all products by " + "page.", content = {
      @Content(mediaType = "application/json")}),
                         @ApiResponse(responseCode = "400", description = "Bad Request / ID not " +
                             "found", content = @Content)})
  @GetMapping("/paged")
  public ResponseEntity<CustomApiResponse<Page<ProductDTO>>> getProductsPaged(
      @RequestParam(defaultValue = "0") final int page, @RequestParam(defaultValue = "10") final int limit,
      @RequestParam(value = "id", required = false) final Integer id,
      @RequestParam(value = "name", required = false) final String name) {
    Pageable pageable = PageRequest.of(page, limit);
    CustomApiResponse<Page<ProductDTO>> response = productService.getAllProductsPag(pageable, id, name);
    return ResponseEntity.ok(response);
  }

  /**
   * Handles the HTTP GET request to retrieve a list of best-selling products for a specific category.
   * This endpoint is mapped to '/category/best-sellers' and expects a request parameter 'categoryId' to
   * specify the category for which the best-sellers should be retrieved. The actual retrieval logic is
   * delegated to the {@code productService.getBestSellersByCategory} method. The response is wrapped in a
   * {@link CustomApiResponse} that includes the HTTP status, a message indicating the result of the operation,
   * and the list of best-selling products as {@link ProductDTO} objects.
   *
   * @param categoryId The category ID for which the best-selling products are requested, passed as a query parameter.
   *                   This must be a valid integer value that corresponds to an existing category in the database.
   *
   * @return A {@link CustomApiResponse} containing the HTTP status {@link}, a message indicating
   * the successful retrieval of best sellers, and the list of {@link ProductDTO} objects representing the
   * best-selling products in the specified category. The structure of the response facilitates easy consumption
   * by clients of the API, ensuring they receive both the data and the context of the request's outcome.
   */
  @GetMapping("/category/best-sellers")
  public CustomApiResponse<List<ProductDTO>> getBestSellersByCategory(final @RequestParam int categoryId) {
    return productService.getBestSellersByCategory(categoryId);
  }


}
