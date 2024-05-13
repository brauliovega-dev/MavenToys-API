package com.oreilly.maventoys.service;

import com.oreilly.maventoys.model.DTO.ProductDTO;
import com.oreilly.maventoys.model.DTO.SaleDTO;
import com.oreilly.maventoys.exceptions.GeneralException;
import com.oreilly.maventoys.exceptions.IdNotFound;
import com.oreilly.maventoys.mapper.ProductMapper;
import com.oreilly.maventoys.mapper.SaleMapper;
import com.oreilly.maventoys.model.entity.Category;
import com.oreilly.maventoys.model.entity.Inventory;
import com.oreilly.maventoys.model.entity.Product;
import com.oreilly.maventoys.model.entity.Sale;
import com.oreilly.maventoys.repository.CategoryRepository;
import com.oreilly.maventoys.repository.InventoryRepository;
import com.oreilly.maventoys.repository.ProductRepository;
import com.oreilly.maventoys.model.CustomApiResponse;
import com.oreilly.maventoys.model.DTO.StockResponse;
import com.oreilly.maventoys.repository.specifications.ProductSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for handling product-related operations within the application.
 * This includes the management of product records such as creating, retrieving, updating,
 * and deleting product data, as well as handling inventory and sales data associated with products.
 * The service uses ProductRepository for persistence operations and ProductMapper for
 * converting between entity and DTO representations. CategoryRepository and InventoryRepository
 * are also utilized for handling product categories and stock levels, respectively.
 */
@Service
@RequiredArgsConstructor
public class ProductService {


  /**
   * Repository for managing product data.
   */
  private final ProductRepository productRepository;


  /**
   * Repository for managing category data.
   */
  private final CategoryRepository categoryRepository;


  /**
   * Repository for managing inventory data.
   */
  private final InventoryRepository inventoryRepository;

  /**
   * Repository for managing product data.
   */
  private final ProductMapper productMapper;

  /**
   * Repository for managing product data.
   */
  private final SaleMapper saleMapper;

  /**
   * Retrieves all products currently marked as active in the database and converts them to a collection of
   * {@link ProductDTO} objects.
   * This operation filters only those products that are active, ensuring that any inactive or discontinued products
   * are not included in the response.
   * Each active product is then mapped to a {@link ProductDTO}, facilitating easier data transfer and encapsulation
   * of product information.
   *
   * @return {@link CustomApiResponse <List<ProductDTO>>} containing a list of all active {@link ProductDTO}s along
   * with a success status.
   *
   * @throws GeneralException if an unexpected error occurs during the process of fetching and converting active
   *                          products.
   */
  public CustomApiResponse<List<ProductDTO>> getProducts() {
    try {
      List<Product> activeProducts = productRepository.getByActiveTrue();
      List<ProductDTO> productDTOs =
          activeProducts.stream().map(productMapper::productToProductDTO).collect(Collectors.toList());

      // Construir y devolver ApiResponse directamente desde el servicio
      return new CustomApiResponse<>("Active products retrieved successfully", productDTOs);
    } catch (Exception error) {
      throw new GeneralException("Error fetching all active products: " + "CAUSE: " + error.getCause());
    }
  }


  /**
   * Retrieves the details of a specific product by its unique identifier (ID) and converts it to a
   * {@link ProductDTO} object.
   * This method searches the database for the product corresponding to the provided ID. If the product is found,
   * its information is converted to a {@link ProductDTO}, which is a simplified representation mainly used for data
   * transfer.
   * This DTO is then encapsulated in an {@link CustomApiResponse} object, along with the HTTP status and a
   * descriptive message of the operation's outcome.
   *
   * @param id The ID of the product to be searched. This identifier must be an integer uniquely representing a
   *           product in the database.
   *
   * @return {@link CustomApiResponse <ProductDTO>} containing the HTTP status, a descriptive message, and the
   * {@link ProductDTO} with the product details.
   * The HTTP status is {@code HttpStatus.OK} if the product is successfully found.
   *
   * @throws IdNotFound       Exception thrown if a product with the specified ID is not found.
   *                          The exception contains a detailed message indicating the product's absence.
   * @throws GeneralException Exception thrown if any other problem is encountered during the retrieval operation.
   *                          This includes database access errors, configuration issues, etc.
   *                          The exception includes a descriptive message along with the root cause of the problem.
   */
  public CustomApiResponse<ProductDTO> getProductById(final Integer id) {
    try {
      ProductDTO productDTO = productRepository.findById(id).map(productMapper::productToProductDTO)
                                               .orElseThrow(() -> new IdNotFound("Product not found with ID: " + id));
      return new CustomApiResponse<>("Product details fetched successfully", productDTO);
    } catch (IdNotFound error) {
      throw error;
    } catch (Exception error) {
      throw new GeneralException(
          "A problem was encountered while retrieving the product. " + "CAUSE: " + error.getCause());
    }
  }


  /**
   * Creates a new product record in the database using the information provided in a {@link ProductDTO} and sets its
   * initial inventory stock level.
   * This method first converts the {@link ProductDTO} to a {@link Product} entity, persists it in the database, then
   * creates a new {@link Inventory} record
   * linked to the newly created product with the specified initial stock level. The created product is then
   * converted back to a {@link ProductDTO}
   * for response encapsulation.
   *
   * @param productDTO   The {@link ProductDTO} containing the data for the new product.
   * @param initialStock The initial stock level for the new product.
   *
   * @return {@link CustomApiResponse <ProductDTO>} containing the created product's details as a {@link ProductDTO}.
   *
   * @throws GeneralException if an error occurs during the creation process of the product or inventory record.
   */

  @Transactional
  public CustomApiResponse<ProductDTO> createProduct(final ProductDTO productDTO, final int initialStock) {
    try {
      Product product = productMapper.productDTOToProduct(productDTO);
      Category category = categoryRepository.findById(productDTO.getCategoryId())
                                            .orElseThrow(() -> new IdNotFound("Category ID not found."));
      product.setInventory(createInventory(product, initialStock));
      product.setCategory(category);
      product = productRepository.save(product);

      ProductDTO createdProductDTO = productMapper.productToProductDTO(product);

      return new CustomApiResponse<>("Product created successfully", createdProductDTO);
    } catch (Exception error) {
      throw new GeneralException("Error creating product: " + "CAUSE: " + error.getCause());
    }
  }

  /**
   * Creates a set of inventory items for a given product and stock level.
   * This method initializes a new inventory set, creates an inventory item with the specified
   * product and stock on hand, adds this inventory item to the set, and then returns the set.
   *
   * @param product The product for which the inventory is being created. This cannot be {@code null}.
   * @param stock   The initial stock level for the product. This should be a non-negative integer.
   *
   * @return A set containing a single inventory item with the specified product and stock level.
   */
  private Set<Inventory> createInventory(final Product product, final int stock) {
    Set<Inventory> inventorySet = new HashSet<>();
    Inventory inventory = new Inventory();
    inventory.setProduct(product);
    inventory.setStockOnHand(stock);
    inventorySet.add(inventory);
    return inventorySet;
  }


  /**
   * Updates partial data of a product identified by its ID with the information provided in a {@link ProductDTO}.
   * This method allows for selectively updating product attributes without requiring a full product detail update.
   * It applies only the changes
   * present in the {@link ProductDTO} to the existing product record. If a category ID is provided, it also updates
   * the product's category
   * after validating the existence of the new category.
   *
   * @param id         The ID of the product to update.
   * @param productDTO The {@link ProductDTO} containing the data to patch the product with.
   *
   * @return {@link CustomApiResponse <ProductDTO>} containing the updated product's details as a {@link ProductDTO}.
   *
   * @throws IdNotFound       if the product or related category by the given ID is not found.
   * @throws GeneralException if an error occurs during the update process.
   */
  public CustomApiResponse<ProductDTO> patchProduct(final Integer id, final ProductDTO productDTO) {
    try {
      ProductDTO updatedProductDTO = productRepository.findById(id).map(product -> {

        productMapper.updateProductFromDto(productDTO, product);

        if (productDTO.getCategoryId() != null) {
          Category category = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(
              () -> new IdNotFound("Category not found with ID: " + productDTO.getCategoryId()));
          product.setCategory(category);
        }

        Product updatedProduct = productRepository.save(product);
        return productMapper.productToProductDTO(updatedProduct);
      }).orElseThrow(() -> new IdNotFound("Product not found for the given ID: " + id));

      return new CustomApiResponse<>("Product updated successfully", updatedProductDTO);
    } catch (IdNotFound e) {
      throw e;
    } catch (Exception error) {
      throw new GeneralException("Error updating product: " + "CAUSE: " + error.getCause());
    }
  }


  /**
   * Completely updates a product's information based on the data provided in a {@link ProductDTO}.
   * This method overwrites the existing product details with those contained in the provided {@link ProductDTO},
   * including changing its category if a new category ID is provided.
   * It ensures the product and optionally the new category exist before proceeding with the update.
   *
   * @param id         The ID of the product to update.
   * @param productDTO The {@link ProductDTO} containing the new data for the product.
   *
   * @return {@link CustomApiResponse <ProductDTO>} containing the updated product's details as a {@link ProductDTO}.
   *
   * @throws IdNotFound       if the product or related category by the given ID is not found.
   * @throws GeneralException if an error occurs during the product's update process.
   */
  public CustomApiResponse<ProductDTO> updateProduct(final Integer id, final ProductDTO productDTO) {
    try {
      Product product =
          productRepository.findById(id).orElseThrow(() -> new IdNotFound("Product not found with ID: " + id));
      productMapper.updateProductFromDto(productDTO, product);

      if (productDTO.getCategoryId() != null) {
        Category category = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(
            () -> new IdNotFound("Category not found with ID: " + productDTO.getCategoryId()));
        product.setCategory(category);
      }

      Product updatedProduct = productRepository.save(product);
      ProductDTO updatedProductDTO = productMapper.productToProductDTO(updatedProduct);

      return new CustomApiResponse<>("Product details updated successfully", updatedProductDTO);
    } catch (IdNotFound idNotFound) {
      throw idNotFound;
    } catch (Exception error) {
      throw new GeneralException("Error updating product: " + "CAUSE: " + error.getCause());
    }
  }


  /**
   * Retrieves the current stock level for a specific product identified by its ID.
   * This method looks up the inventory record associated with the given product ID to fetch the current stock on hand.
   * It is useful for inventory management and stock level monitoring.
   *
   * @param productId The ID of the product to retrieve stock for.
   *
   * @return {@link CustomApiResponse <StockResponse>} containing the stock level as a {@link StockResponse}.
   *
   * @throws IdNotFound       if the product with the specified ID is not found, indicating that the product does not
   *                          exist or has been removed.
   * @throws GeneralException if an error occurs during the fetching of the stock levels.
   */
  public CustomApiResponse<StockResponse> getProductInventory(final Integer productId) {
    try {
      productRepository.findById(productId).orElseThrow(() -> new IdNotFound("Product ID not found: " + productId));
      Integer stock = inventoryRepository.getStockByProductId(productId);
      StockResponse stockResponse = new StockResponse(stock);
      return new CustomApiResponse<>("Stock fetched successfully", stockResponse);
    } catch (IdNotFound e) {
      throw e;
    } catch (Exception error) {
      throw new GeneralException("Error fetching stock: " + "CAUSE: " + error.getCause());
    }
  }

  /**
   * Finds all sales transactions associated with a given product by its ID.
   * This method retrieves a list of sales that involve the specified product, allowing for an analysis of sales
   * performance and history.
   * Each sale is converted to a {@link SaleDTO} to standardize the response format and simplify data transfer.
   *
   * @param productId The ID of the product to find sales for.
   *
   * @return {@link CustomApiResponse <List<SaleDTO>>} containing a list of {@link SaleDTO}s associated with the
   * product.
   *
   * @throws IdNotFound       if no sales are found for the specified product ID, possibly indicating that the
   *                          product has not been sold yet.
   * @throws GeneralException if an error occurs during the retrieval of sales data.
   */
  public CustomApiResponse<List<SaleDTO>> getSalesByProductId(final Integer productId) {
    try {
      List<Sale> sales = productRepository.getAllSalesByProductId(productId);
      if (sales.isEmpty()) {
        throw new IdNotFound("No sales found for product ID: " + productId);
      }
      List<SaleDTO> saleDTOs = sales.stream().map(saleMapper::saleToSaleDTO).collect(Collectors.toList());
      return new CustomApiResponse<>("Sale data for product retrieved successfully", saleDTOs);
    } catch (IdNotFound e) {
      throw e;
    } catch (Exception e) {
      throw new GeneralException("Error retrieving sales data: " + "CAUSE: " + e.getCause());
    }
  }


  /**
   * Retrieves the price history for a specific product by its ID.
   * It searches for all historical price changes for the product, facilitating trend analysis and price adjustment
   * decisions.
   * This method is particularly useful for tracking the fluctuation in product pricing over time.
   *
   * @param productId The ID of the product for which to retrieve price history.
   *
   * @return {@link CustomApiResponse <List<ProductDTO>>} containing a list of {@link ProductDTO}s representing the
   * price history.
   *
   * @throws IdNotFound       if no product with the specified ID is found, indicating a possible error or that the
   *                          product does not exist.
   * @throws GeneralException if an error occurs during the retrieval of the price history.
   */
  public CustomApiResponse<List<ProductDTO>> getPriceHistoryByProductId(final Integer productId) {
    try {
      Product product = productRepository.findById(productId).orElseThrow(
          () -> new IdNotFound("Product with ID: " + productId + " not found."));
      List<Product> products = productRepository.getProductByName(product.getName());
      List<ProductDTO> productDTOs =
          products.stream().map(productMapper::productToProductDTO).collect(Collectors.toList());
      return new CustomApiResponse<>("Price history retrieved successfully", productDTOs);
    } catch (IdNotFound e) {
      throw e;
    } catch (Exception e) {
      throw new GeneralException("Error retrieving price history: " + "CAUSE: " + e.getCause());
    }
  }


  /**
   * Retrieves a paginated list of products based on the provided specifications.
   * This method uses the provided Pageable object to determine the size and number of the page to be returned.
   * It also uses the provided id and name to create a ProductSpec object, which is used to filter the products.
   * The filtered and paginated products are then converted to their DTO forms and returned in a CustomApiResponse.
   *
   * @param pageable a Pageable object containing the pagination information.
   * @param id       an Integer representing the id of the product to be included in the filter. Can be null.
   * @param name     a String representing the name of the product to be included in the filter. Can be null.
   *
   * @return a CustomApiResponse containing a Page of ProductDTO objects representing the filtered and paginated
   * products.
   *
   * @throws GeneralException if there is an error during the retrieval process. This exception includes a detailed
   * cause of the failure.
   */
  public CustomApiResponse<Page<ProductDTO>> getAllProductsPag(final Pageable pageable, final Integer id,
                                                               final String name) {
    try {
      ProductSpec specification = new ProductSpec(id, name);
      Page<Product> productPage = productRepository.findAll(specification, pageable);
      Page<ProductDTO> productDTOPage = productPage.map(productMapper::productToProductDTO);
      return new CustomApiResponse<>("Paged products retrieved successfully", productDTOPage);
    } catch (Exception error) {
      throw new GeneralException("Error retrieving paged products: " + "CAUSE: " + error.getCause());
    }
  }

  /**
   * Retrieves a list of best-selling products in a specific category. This method queries the
   * database for products marked as best sellers within the given category ID. The products
   * are then converted to their Data Transfer Object (DTO) forms for external use. The result
   * is wrapped in a {@link CustomApiResponse} object that includes both the message indicating
   * the successful retrieval and the list of product DTOs.
   *
   * @param categoryId the ID of the category for which best-selling products are to be fetched.
   *                   This must be a valid category ID present in the database.
   *
   * @return {@link CustomApiResponse} containing a message and a list of {@link ProductDTO}
   * objects representing the best-selling products in the specified category. The
   * message "Best sellers retrieved successfully" is included in the response to
   * indicate the operation's success.
   *
   * @throws GeneralException if there is an error during the retrieval process. This exception
   *                          includes a detailed cause of the failure, making it easier to diagnose and
   *                          troubleshoot the issue. The exception message is formatted as "Error retrieving
   *                          best sellers: CAUSE: " followed by the cause of the error.
   */
  public CustomApiResponse<List<ProductDTO>> getBestSellersByCategory(final int categoryId) {
    try {
      List<Product> productList = productRepository.findBestSellersByCategory(categoryId);
      List<ProductDTO> productDTOList =
          productList.stream().map(productMapper::productToProductDTO).collect(Collectors.toList());
      return new CustomApiResponse<>("Best sellers retrieved successfully", productDTOList);
    } catch (Exception e) {
      throw new GeneralException("Error retrieving best sellers: " + "CAUSE: " + e.getCause());
    }
  }


}
