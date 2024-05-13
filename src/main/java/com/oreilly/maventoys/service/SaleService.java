package com.oreilly.maventoys.service;

import com.oreilly.maventoys.model.DTO.SaleDTO;
import com.oreilly.maventoys.exceptions.GeneralException;
import com.oreilly.maventoys.exceptions.IdNotFound;
import com.oreilly.maventoys.mapper.SaleMapper;
import com.oreilly.maventoys.model.entity.Employee;
import com.oreilly.maventoys.model.entity.Invoice;
import com.oreilly.maventoys.model.entity.Product;
import com.oreilly.maventoys.model.entity.Sale;
import com.oreilly.maventoys.model.entity.Store;
import com.oreilly.maventoys.repository.EmployeeRepository;
import com.oreilly.maventoys.repository.ProductRepository;
import com.oreilly.maventoys.repository.SaleRepository;
import com.oreilly.maventoys.repository.StoreRepository;
import com.oreilly.maventoys.model.CustomApiResponse;
import com.oreilly.maventoys.repository.specifications.SaleSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Service layer responsible for managing sales data within the application.
 * It encompasses functionalities such as retrieving, creating, and updating sales records,
 * as well as associating sales with specific stores and employees. This service leverages
 * data access objects to interact with the database, ensuring data integrity and facilitating
 * business operations related to sales.
 */
@Service
@RequiredArgsConstructor
public class SaleService {
  /**
   * Repository for managing sales records. It provides CRUD operations on sales,
   * enabling the service to retrieve sales data, create new sales records, and update
   * existing sales information within the database.
   */
  private final SaleRepository saleRepository;


  /**
   * Repository for managing store information. This is used to validate and associate
   * sales records with specific stores, ensuring that each sale is correctly linked
   * to a physical store location within the application's data model.
   */
  private final StoreRepository storeRepository;


  /**
   * Repository for managing employee data. Utilized within the sales context to
   * associate sales records with employees, facilitating tracking of sales by employee
   * and enabling sales performance analysis and management.
   */
  private final EmployeeRepository employeeRepository;

  /**
   * Repository for managing product data. Utilized within the sales context to
   * associate sales records with employees, facilitating tracking of sales by employee
   * and enabling sales performance analysis and management.
   */
  private final ProductRepository productRepository;

  /**
   * Mapper for managing Sale data.
   */
  private final SaleMapper saleMapper;

  /**
   * Retrieves all sales records from the database with pagination support.
   * This method is designed to efficiently handle large volumes of sales data
   * by applying pagination, thereby enhancing performance and user experience.
   * Each sale record is converted to a SaleDTO to facilitate easy data handling
   * and encapsulation.
   *
   * @param pageable Pagination information to manage the volume of returned data.
   *
   * @return ApiResponse containing a paginated list of SaleDTOs with a success status,
   * indicating successful retrieval of sales data.
   *
   * @throws GeneralException if an unexpected error occurs during the fetching process,
   *                          encapsulating any underlying database or application issues.
   */
  public CustomApiResponse<Page<SaleDTO>> getAllSales(final Pageable pageable) {
    try {
      Page<SaleDTO> allSales = saleRepository.findAll(pageable).map(saleMapper::saleToSaleDTO);
      return new CustomApiResponse<>("All sales fetched successfully", allSales);
    } catch (Exception error) {
      throw new GeneralException("Error fetching all sales: CAUSE: " + "CAUSE: " + error.getCause());
    }
  }


  /**
   * Retrieves the details of a specific sale by its unique identifier (ID).
   * This method ensures accurate and efficient access to the sales record,
   * converting the entity to a SaleDTO for standardized data handling.
   *
   * @param id The unique identifier of the sale to retrieve.
   *
   * @return ApiResponse containing the SaleDTO and a success status, if found.
   *
   * @throws IdNotFound       if no sale with the specified ID is found, indicating
   *                          the absence of the sale record in the database.
   * @throws GeneralException if an unforeseen problem occurs during retrieval,
   *                          encapsulating any underlying issue.
   */
  public CustomApiResponse<SaleDTO> getSalesById(final Integer id) {
    try {
      SaleDTO saleDTO = saleRepository.findById(id).map(saleMapper::saleToSaleDTO)
                                      .orElseThrow(() -> new IdNotFound("Sale with ID " + id + " not found."));
      return new CustomApiResponse<>("Sale details fetched successfully", saleDTO);
    } catch (IdNotFound error) {
      throw error;
    } catch (Exception error) {
      throw new GeneralException(
          "A problem was encountered while retrieving the sale. CAUSE: " + "CAUSE: " + error.getCause());
    }
  }


  /**
   * Creates a list of {@link Invoice} objects for a given sale. Each invoice is generated based on the products
   * included in the sale, with each product's ID used to fetch the product details. An invoice is then
   * created for each product with the specified quantity and discount.
   *
   * @param saleDTO The {@link SaleDTO} object containing the details of the sale, including the products to be
   *                included in the sale.
   * @param sale    The {@link Sale} entity that represents the sale in the database. This entity is used to
   *                link the created invoices to the specific sale.
   *
   * @return A list of {@link Invoice} objects, each representing an invoice for a product included in the sale.
   * This list is then used to update the {@link Sale} entity with the invoices for the sale.
   */
  private List<Invoice> createInvoices(final SaleDTO saleDTO, final Sale sale) {
    List<Invoice> invoices = new ArrayList<>();
    saleDTO.getProducts().forEach(p -> {
      Product product = fetchProduct(p.getProduct_id());
      Invoice invoice = newInvoice(product, p.getQuantity(), p.getDiscount(), sale);
      invoices.add(invoice);
    });
    return invoices;
  }

  /**
   * Processes a sale transaction by converting a {@link SaleDTO} to a {@link Sale} entity, generating invoices
   * for each product in the sale, calculating the total sale amount after discounts, and saving the sale
   * information to the database.
   *
   * @param saleDTO The {@link SaleDTO} object containing the details of the sale, including the products to be
   *                sold, their quantities, and applicable discounts.
   *
   * @return A {@link CustomApiResponse} that wraps the resulting {@link SaleDTO} with the status of the operation
   * and a message. This response object is used to convey the outcome of the sale creation process to
   * the caller, including any relevant data about the created sale.
   *
   * @implNote This method starts by mapping the {@link SaleDTO} to a {@link Sale} entity. It then assigns invoices
   * to the sale by calling {@link #createInvoices(SaleDTO, Sale)}. The total for the sale is calculated by
   * iterating over the invoices, taking into account any discounts. Finally, the sale is saved to the
   * database, and a response is generated and returned.
   * @see SaleMapper#saleDTOToSale(SaleDTO) Method to map {@link SaleDTO} to {@link Sale}.
   * @see #createInvoices(SaleDTO, Sale) Method to create and assign invoices to the sale.
   * @see SaleMapper#saleToSaleDTO(Sale) Method to convert {@link Sale} entity back to {@link SaleDTO}.
   */
  public CustomApiResponse<SaleDTO> createSale(final SaleDTO saleDTO) {
    Sale sale = saleMapper.saleDTOToSale(saleDTO); // mapeo inicial de SaleDTO a Sale
    sale.setInvoices(createInvoices(saleDTO, sale)); // creacion y asignacion de las facturas
    // calcular  total de la venta
    final int hundredPercent = 100;
    Double total = sale.getInvoices().stream().mapToDouble(
        invoice -> invoice.getSubtotal() - (invoice.getSubtotal() * invoice.getDiscount() / hundredPercent)).sum();
    sale.setTotal(total);
    // guardar la venta en la base de datos y retornar respuesta
    return new CustomApiResponse<>("Sale created successfully", saleMapper.saleToSaleDTO(saleRepository.save(sale)));
  }


  /**
   * Creates a new invoice for a product within a sale transaction.
   * This method calculates the subtotal for the product based on the quantity and product price.
   *
   * @param product  The product being sold.
   * @param quantity The quantity of the product being sold.
   * @param discount The discount applied to the product, expressed as a percentage.
   * @param sale     The sale transaction the product is part of.
   *
   * @return An {@link Invoice} with the details of the product sale.
   */
  public Invoice newInvoice(final Product product, final int quantity, final int discount, final Sale sale) {
    Invoice invoice = new Invoice();
    invoice.setProduct(product);
    invoice.setSale(sale);
    invoice.setDiscount(discount);
    invoice.setQuantity(quantity);
    invoice.setSubtotal(quantity * product.getPrice());
    return invoice;
  }

  /**
   * Fetches a product by its ID.
   * Throws a {@link RuntimeException} if the product cannot be found.
   *
   * @param id The ID of the product to fetch.
   *
   * @return The {@link Product} with the given ID.
   *
   * @throws RuntimeException if the product with the specified ID is not found.
   */
  private Product fetchProduct(final Integer id) {
    return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
  }


  /**
   * Updates an existing sale record identified by its ID, using the data provided in the SaleDTO.
   * This method ensures that only specified fields are updated, preserving any other existing
   * sale data. It validates associated entities such as Store and Employee, linking them
   * accordingly.
   *
   * @param id      The unique identifier of the sale to update.
   * @param saleDTO The SaleDTO containing updated data for the sale.
   *
   * @return ApiResponse containing the updated SaleDTO and a success status.
   *
   * @throws IdNotFound       if the sale or associated entities are not found, indicating
   *                          a reference integrity issue.
   * @throws GeneralException if an error occurs during the update process,
   *                          encapsulating any underlying issue.
   */
  public CustomApiResponse<SaleDTO> updateSale(final Integer id, final SaleDTO saleDTO) {
    try {
      Sale sale = saleRepository.findById(id).orElseThrow(() -> new IdNotFound("Sale not found with ID: " + id));
      saleMapper.updateSaleFromDto(saleDTO, sale);

      if (saleDTO.getStoreId() != null) {
        Store store = storeRepository.findById(saleDTO.getStoreId()).orElseThrow(
            () -> new IdNotFound("Store with ID " + saleDTO.getStoreId() + " not found."));
        sale.setStore(store);
      }

      if (saleDTO.getEmployeeId() != null) {
        Employee employee = employeeRepository.findById(saleDTO.getEmployeeId()).orElseThrow(
            () -> new IdNotFound("Employee with ID " + saleDTO.getEmployeeId() + " not found."));
        sale.setEmployee(employee);
      }

      sale = saleRepository.save(sale);
      SaleDTO resultDTO = saleMapper.saleToSaleDTO(sale);
      return new CustomApiResponse<>("Sale updated successfully", resultDTO);
    } catch (IdNotFound idNotFound) {
      throw idNotFound;
    } catch (Exception error) {
      throw new GeneralException("Error updating sale: " + "CAUSE: " + error.getCause());
    }
  }


  /**
   * Finds sales records within a specified date range, converting each to SaleDTO.
   * This method facilitates analysis and reporting by allowing retrieval of sales data
   * based on temporal criteria. It is particularly useful for performance assessment
   * and trend analysis over specific periods.
   *
   * @param startDate The start date of the range.
   * @param endDate   The end date of the range.
   *
   * @return ApiResponse containing a list of SaleDTOs within the date range and a success status.
   *
   * @throws GeneralException if an error occurs during the fetching process,
   *                          encapsulating any underlying database or application issues.
   */
  public CustomApiResponse<List<SaleDTO>> getSalesByDate(final LocalDate startDate, final LocalDate endDate) {
    try {
      List<Sale> sales = saleRepository.findByDateBetween(startDate, endDate);

      List<SaleDTO> salesDTOs = sales.stream().map(saleMapper::saleToSaleDTO).collect(Collectors.toList());

      return new CustomApiResponse<>("Sale fetched successfully", salesDTOs);
    } catch (Exception error) {
      throw new GeneralException("Error fetching sales by date range: " + "CAUSE: " + error.getCause());
    }
  }

  /**
   * Retrieves a paged list of sales filtered by the provided parameters.
   *
   * @param pageable The pagination information.
   * @param id The id of the sale to be retrieved.
   * @param storeId The id of the store related to the sales.
   * @param employeeId The id of the employee related to the sales.
   * @return A CustomApiResponse containing a paged list of SaleDTO objects.
   * @throws GeneralException if an error occurs while retrieving the sales.
   */
  public CustomApiResponse<Page<SaleDTO>> getAllSalesPaged(final Pageable pageable, final Integer id,
                                                           final Integer storeId, final Integer employeeId) {
    try {
      SaleSpec specifications = new SaleSpec(id, storeId, employeeId);
      Page<Sale> salePage = saleRepository.findAll(specifications, pageable);
      Page<SaleDTO> resultDTOPage = salePage.map(saleMapper::saleToSaleDTO);
      return new CustomApiResponse<>("All sales retrieved successfully", resultDTOPage);
    } catch (Exception error) {
      throw new GeneralException("Error finding all sales" + "CAUSE: " + error.getCause());
    }
  }


}
