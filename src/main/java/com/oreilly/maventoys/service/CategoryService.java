package com.oreilly.maventoys.service;

import com.oreilly.maventoys.model.DTO.CategoryDTO;
import com.oreilly.maventoys.model.DTO.ProductDTO;
import com.oreilly.maventoys.exceptions.GeneralException;
import com.oreilly.maventoys.exceptions.IdNotFound;
import com.oreilly.maventoys.mapper.CategoryMapper;
import com.oreilly.maventoys.mapper.ProductMapper;
import com.oreilly.maventoys.model.entity.Category;
import com.oreilly.maventoys.model.entity.Product;
import com.oreilly.maventoys.repository.CategoryRepository;
import com.oreilly.maventoys.repository.ProductRepository;
import com.oreilly.maventoys.model.CustomApiResponse;
import com.oreilly.maventoys.repository.specifications.CategorySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for managing category-related operations within the application.
 * This includes creating, retrieving, and updating category records, as well as
 * managing product associations. Utilizes CategoryRepository for category persistence
 * and ProductRepository for product-related operations, with CategoryMapper and ProductMapper
 * for DTO conversions.
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

  /**
   * Repository for managing category data. Used for CRUD operations on categories,
   * including querying categories by their active status.
   */
  private final CategoryRepository categoryRepository;


  /**
   * Repository for managing product data. This is utilized for operations
   * that involve fetching products associated with a specific category.
   */
  private final ProductRepository productRepository;

  /**
   * Repository for managing product data.
   */
  private final ProductMapper productMapper;

  /**
   * Repository for managing category data.
   */
  private final CategoryMapper categoryMapper;


  /**
   * Retrieves all active categories from the database and converts them to CategoryDTOs.
   * This method is designed to fetch categories that are marked active, ensuring that
   * only relevant data is returned to the client. The conversion to DTOs aids in
   * abstracting the domain model and facilitating easier data handling.
   *
   * @return ApiResponse containing a list of CategoryDTOs for active categories
   * and a success status, indicating successful retrieval.
   *
   * @throws GeneralException if an unexpected error occurs during the retrieval process,
   *                          encapsulating any underlying database or application issues.
   */
  public CustomApiResponse<List<CategoryDTO>> getAllCategories() {
    try {
      List<Category> activeCategories = categoryRepository.findByActiveTrue();
      List<CategoryDTO> categoryDTOs =
          activeCategories.stream().map(categoryMapper::categoryToCategoryDTO).collect(Collectors.toList());
      return new CustomApiResponse<>("Categories retrieved successfully", categoryDTOs);
    } catch (Exception error) {
      throw new GeneralException("Error finding all active categories: " + "CAUSE: " + error.getCause());
    }
  }


  /**
   * Creates a new category in the database based on the provided CategoryDTO.
   * This method handles the conversion from DTO to entity, saves the new category,
   * and then converts the saved entity back to DTO for the response.
   *
   * @param categoryDTO The DTO containing the data for the new category.
   *
   * @return ApiResponse containing the newly created CategoryDTO and a success status.
   *
   * @throws GeneralException if an error occurs during the creation process,
   *                          encapsulating any underlying database or data integrity issues.
   */
  public CustomApiResponse<CategoryDTO> createCategory(final CategoryDTO categoryDTO) {
    try {
      Category category = categoryMapper.categoryDTOToCategory(categoryDTO);
      category = categoryRepository.save(category);
      return new CustomApiResponse<>("Category created successfully", categoryMapper.categoryToCategoryDTO(category));
    } catch (Exception error) {
      throw new GeneralException("Error creating category: " + "CAUSE: " + error.getCause());
    }
  }


  /**
   * Retrieves the details of a specific category by its unique identifier.
   * Ensures efficient access to category information, returning it as a CategoryDTO.
   *
   * @param id The unique identifier of the category to retrieve.
   *
   * @return ApiResponse containing the CategoryDTO and a success status, if found.
   *
   * @throws IdNotFound       if the category with the specified ID is not found,
   *                          indicating the absence of the category in the database.
   * @throws GeneralException if an unforeseen problem occurs during retrieval,
   *                          encapsulating any underlying issue.
   */
  public CustomApiResponse<CategoryDTO> getCategoryById(final Integer id) {
    try {
      Category category =
          categoryRepository.findById(id).orElseThrow(() -> new IdNotFound("Category not found with the ID: " + id));
      CategoryDTO categoryDTO = categoryMapper.categoryToCategoryDTO(category);
      return new CustomApiResponse<>("Category details fetched successfully", categoryDTO);
    } catch (IdNotFound e) {
      return new CustomApiResponse<>(e.getMessage(), null);
    } catch (Exception error) {
      throw new GeneralException("Error finding category by ID: " + "CAUSE: " + error.getCause());
    }
  }


  /**
   * Updates an existing category identified by its ID with data provided in a CategoryDTO.
   * Facilitates partial updates to category details, preserving unchanged data.
   *
   * @param id          The unique identifier of the category to update.
   * @param categoryDTO The CategoryDTO containing updated data for the category.
   *
   * @return ApiResponse containing the updated CategoryDTO and a success status.
   *
   * @throws IdNotFound       if the category is not found, indicating a reference integrity issue.
   * @throws GeneralException if an error occurs during the update process,
   *                          encapsulating any underlying issue.
   */
  public CustomApiResponse<CategoryDTO> patchCategory(final Integer id, final CategoryDTO categoryDTO) {
    try {
      Category category = categoryRepository.findById(id).map(existingCategory -> {
        categoryMapper.updateCategoryFromDto(categoryDTO, existingCategory);
        return categoryRepository.save(existingCategory);
      }).orElseThrow(() -> new IdNotFound("Category not found with the ID: " + id));
      CategoryDTO updatedCategoryDTO = categoryMapper.categoryToCategoryDTO(category);
      return new CustomApiResponse<>("Category updated successfully", updatedCategoryDTO);
    } catch (IdNotFound e) {
      return new CustomApiResponse<>(e.getMessage(), null);
    } catch (Exception error) {
      throw new GeneralException("Error updating category: " + "CAUSE: " + error.getCause());
    }
  }


  /**
   * Finds all products associated with a given category ID, converting each to ProductDTO.
   * Useful for retrieving all products under a specific category for catalog displays
   * or inventory management.
   *
   * @param categoryId The unique identifier of the category whose products are to be retrieved.
   *
   * @return ApiResponse containing a list of ProductDTOs for the specified category
   * and a success status.
   *
   * @throws IdNotFound       if no products are found for the given category ID, indicating
   *                          a potential data integrity issue or an empty category.
   * @throws GeneralException if an error occurs during the retrieval process,
   *                          encapsulating any underlying database or application issues.
   */
  public CustomApiResponse<List<ProductDTO>> findProductsByCategoryId(final Integer categoryId) {
    try {
      List<Product> products = productRepository.getByCategoryId(categoryId);
      if (products.isEmpty()) {
        throw new IdNotFound("No products found for category ID: " + categoryId);
      }
      List<ProductDTO> productDTOs =
          products.stream().map(productMapper::productToProductDTO).collect(Collectors.toList());
      return new CustomApiResponse<>("Products data for the category retrieved successfully", productDTOs);
    } catch (IdNotFound e) {
      return new CustomApiResponse<>(e.getMessage(), null);
    } catch (Exception error) {
      throw new GeneralException("Error finding products by category ID: " + "CAUSE: " + error.getCause());
    }
  }

  /**
    * Retrieves a paged list of categories based on the provided parameters.
    * This method fetches categories from the repository using the specified page and size,
    * along with optional filtering parameters for ID and name. The method then maps the
    * retrieved categories to CategoryDTOs for a structured response.
    *
    * @param pageable The pagination information for the category list.
    * @param id       The unique identifier of the category to filter by.
    * @param name     The name of the category to filter by.
    *
    * @return ApiResponse containing a paged list of CategoryDTOs and a success status.
    *
    * @throws GeneralException if an error occurs during the retrieval process,
    *                          encapsulating any underlying database or application issues.
    */
  public CustomApiResponse<Page<CategoryDTO>> getCategoriesPaged(final Pageable pageable, final Integer id,
                                                                 final String name) {
    try {
      CategorySpec spec = new CategorySpec(id, name);
      Page<Category> categoryPage = categoryRepository.findAll(spec, pageable);
      Page<CategoryDTO> resultDTOPage = categoryPage.map(categoryMapper::categoryToCategoryDTO);
      return new CustomApiResponse<>("All categories retrieved successfully", resultDTOPage);
    } catch (Exception error) {
      throw new GeneralException("Error finding all categories: " + "CAUSE: " + error.getCause());
    }
  }

  /**
   * Retrieves sales data for all categories.
   * <p>
   * This method fetches an array of sales data for each category from the repository,
   * then maps each array to a {@link CategoryDTO} object. Each {@link CategoryDTO} contains
   * the category's ID, name, and total sales. The method ensures data integrity by checking
   * array length before accessing total sales data to avoid index out of bounds exceptions.
   * </p>
   * <p>
   * The conversion process encapsulates the raw database results into a more structured
   * form, making the data easier to handle and interpret in the business logic or presentation layers.
   * </p>
   *
   * @return a {@link CustomApiResponse} containing a list of {@link CategoryDTO}s representing
   * each category's sales data. The response includes the category ID, name, and total
   * sales amount, providing a comprehensive overview of sales distribution among categories.
   */
  public CustomApiResponse<List<CategoryDTO>> getCategorySales() {
    List<Object[]> results = categoryRepository.findCategorySales();
    List<CategoryDTO> categorySales = new ArrayList<>();
    for (Object[] result : results) {
      CategoryDTO dto = new CategoryDTO();
      dto.setId(((Number) result[0]).intValue());
      dto.setName((String) result[1]);
      if (result.length > 2) {
        dto.setTotalSales(((Number) result[2]).doubleValue());
      }
      categorySales.add(dto);
    }
    return new CustomApiResponse<>("Success", categorySales);
  }


}
