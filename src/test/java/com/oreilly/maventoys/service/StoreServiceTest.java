package com.oreilly.maventoys.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import com.oreilly.maventoys.exceptions.GeneralException;
import com.oreilly.maventoys.exceptions.IdNotFound;
import com.oreilly.maventoys.mapper.EmployeeMapper;
import com.oreilly.maventoys.mapper.SaleMapper;
import com.oreilly.maventoys.mapper.StoreMapper;
import com.oreilly.maventoys.model.CustomApiResponse;
import com.oreilly.maventoys.model.DTO.EmployeeDTO;
import com.oreilly.maventoys.model.DTO.SaleDTO;
import com.oreilly.maventoys.model.DTO.StoreDTO;
import com.oreilly.maventoys.model.entity.Employee;
import com.oreilly.maventoys.model.entity.Sale;
import com.oreilly.maventoys.model.entity.Store;
import com.oreilly.maventoys.repository.EmployeeRepository;
import com.oreilly.maventoys.repository.SaleRepository;
import com.oreilly.maventoys.repository.StoreRepository;
import com.oreilly.maventoys.repository.specifications.StoreSpec;
import com.oreilly.maventoys.service.StoreService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

// FLujo: simulacion de tiendas activas,
// se obtiene una lista de tiendas activas, se mapea a un DTO y se retorna
// se verifica que la respuesta no sea nula,
// que el mensaje sea el esperado,
// que la lista de datos no este vacia
// y que el nombre de la tienda sea el esperado

@ExtendWith(MockitoExtension.class) //
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class) // guardar anotacion
public class StoreServiceTest {

  @Mock // mock (imitar) imita el comportamiento de un componente real de una aplicación
  private StoreRepository storeRepository;

  @Mock
  private StoreMapper storeMapper;

  @Mock
  private EmployeeRepository employeeRepository;

  @Mock
  private EmployeeMapper employeeMapper;

  @Mock
  private SaleMapper saleMapper;

  @Mock
  private SaleRepository saleRepository;

  @InjectMocks  // inyecta los mocks (store mapper y store repository) en la clase de prueba (store service)
  private StoreService storeService;

  private static int storeId;

  @BeforeAll
  static void setUp() {
    int storeId = 1;
  }

  //  test que se encarga de verificar que se obtienen las tiendas activas
  @Test
  @DisplayName("GetStores Test")
  void getStoreTest() {
    // config inicial
    Store store = new Store();
    StoreDTO storeDTO = new StoreDTO();
    store = new Store();
    storeDTO = new StoreDTO();
    store.setName("Test Store");
    store.setCity("Test City");
    store.setLocation("Test Location");
    store.setActive(true);

    storeDTO.setName("Test Store");
    storeDTO.setCity("Test City");

    //en esta línea, estamos diciendo: "cuando el método getByActiveTrue() del objeto storeRepository sea llamado,
    // entonces retorna una lista del objeto que acabamos de setear arriba." Esto simula que la base de datos tiene
    // una tienda activa sin necesidad de interactuar con la base de datos real
    when(storeRepository.getByActiveTrue()).thenReturn(Arrays.asList(store));
    when(storeMapper.storeToStoreDTO(store)).thenReturn(storeDTO);

    // llamada al metodo
    CustomApiResponse<List<StoreDTO>> response = storeService.getStores();

    // verificacion no nullo, mensaje esperado, lista de datos no vacia, nombre de tienda esperado
    assertNotNull(response, "The response should not be null");
    assertEquals("Active store details fetched successfully", response.getMessage(), "Unexpected response message");
    assertFalse(response.getData().isEmpty(), "The data list should not be empty");
    assertEquals("Test Store", response.getData().get(0).getName(), "Store name does not match expected");

    // verificaciones de interac
    verify(storeRepository).getByActiveTrue();
    verify(storeMapper).storeToStoreDTO(store);
  }


  @Test
  @DisplayName("GetStores cuando no hay tiendas")

    //cambiar por otra exepcion adecuada entity not foud por ejenmplo
  void getStores_WhenNoActiveStores() {
    // Setup
    when(storeRepository.getByActiveTrue()).thenReturn(Collections.emptyList());

    Exception exception = assertThrows(EntityNotFoundException.class, () -> {
      storeService.getStores();
    });

    String expectedMessage = "No active stores found";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  @DisplayName("GetStores con exception ")
  void getStores_WhenErrorOccurs() {

    when(storeRepository.getByActiveTrue()).thenThrow(new RuntimeException());

    Exception exception = assertThrows(GeneralException.class, () -> {
      storeService.getStores();
    });

    String expectedMessage = "Error fetching all active stores: ";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void getStoreByIdTest() {
    Integer storeId = 1;
    Store store = new Store();
    store.setId(storeId);
    store.setName("Test Store");

    StoreDTO storeDTO = new StoreDTO();
    storeDTO.setName("Test Store");

    when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
    when(storeMapper.storeToStoreDTO(store)).thenReturn(storeDTO);

    CustomApiResponse<StoreDTO> response = storeService.getStoreById(storeId);

    assertNotNull(response, "The response should not be null");
    assertEquals("Store details fetched successfully", response.getMessage(), "Unexpected response message");
    assertNotNull(response.getData(), "The store data should not be null");
    assertEquals("Test Store", response.getData().getName(), "Store name does not match expected");


    verify(storeRepository).findById(storeId);
    verify(storeMapper).storeToStoreDTO(store);
  }


  @Test
  public void getStoreById_WhenStoreIsNotFound() {
    // config
    Integer storeId = 1;
    when(storeRepository.findById(storeId)).thenReturn(Optional.empty());
    // cuando se llame el metodo findById del
    // store repository con el id 1, simulamos
    // que no se encuentra la tienda y retorna un optional vacio


    assertThrows(IdNotFound.class, () -> {
      storeService.getStoreById(storeId);
    }, "Expected IdNotFound to be thrown if store is not found");
  }


  @Test
  void getStoreById_WhenErrorOccurs() {
    Integer storeId = 1;
    when(storeRepository.findById(storeId)).thenThrow(
        new GeneralException("A problem was encountered while retrieving the store. "));

    Exception exception = assertThrows(GeneralException.class, () -> {
      storeService.getStoreById(storeId);
    });
    String expectedMessage = "A problem was encountered while retrieving the store. ";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void createStore_WhenStoreIsCreatedSuccessfully() {

    //givwen
    StoreDTO storeDTO = new StoreDTO();
    storeDTO.setName("Test Store");

    Store store = new Store();
    store.setName("Test Store");

    when(storeMapper.storeDTOToStore(storeDTO)).thenReturn(store);
    when(storeRepository.save(store)).thenReturn(store);
    when(storeMapper.storeToStoreDTO(store)).thenReturn(storeDTO);

    CustomApiResponse<StoreDTO> response = storeService.createStore(storeDTO);

    assertNotNull(response, "The response should not be null");
    assertEquals("Store created successfully", response.getMessage(), "Unexpected response message");
    assertNotNull(response.getData(), "The store data should not be null");
    assertEquals("Test Store", response.getData().getName(), "Store name does not match expected");

    verify(storeMapper).storeDTOToStore(storeDTO);
    verify(storeRepository).save(store);
    verify(storeMapper).storeToStoreDTO(store);
  }
//cprreccion  de la excepcion
  @Test
  void createStore_WhenErrorOccurs() {
    StoreDTO storeDTO = new StoreDTO();
    storeDTO.setName("Test Store");
    storeDTO.setCity("Test City");

    Store store = new Store();
    store.setName("Test Store");

    when(storeMapper.storeDTOToStore(storeDTO)).thenReturn(store);
    when(storeRepository.save(store)).thenThrow(new RuntimeException());

    Exception exception = assertThrows(GeneralException.class, () -> {
      storeService.createStore(storeDTO);
    });
    String expectedMessage = "Error creating store: ";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));

    verify(storeMapper).storeDTOToStore(storeDTO);
    verify(storeRepository).save(store);
  }

  @Test
  @DisplayName("Patch Store Successfully")
  void patchStore_Success() {
    Integer storeId = 1;
    StoreDTO storeDTO = new StoreDTO();
    storeDTO.setName("Updated Store");

    Store store = new Store();
    store.setName("Test Store");

    when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
    when(storeRepository.save(any(Store.class))).thenReturn(store);
    when(storeMapper.storeToStoreDTO(store)).thenReturn(storeDTO);

    CustomApiResponse<StoreDTO> response = storeService.patchStore(storeId, storeDTO);

    assertNotNull(response, "The response should not be null");
    assertEquals("Store updated successfully", response.getMessage(), "Unexpected response message");
    assertNotNull(response.getData(), "The store data should not be null");
    assertEquals("Updated Store", response.getData().getName(), "Store name does not match expected");

    verify(storeRepository).findById(storeId);
    verify(storeMapper).updateStoreFromDto(storeDTO, store);
    verify(storeRepository).save(any(Store.class));
  }

  @Test
  void patchStore_WhenStoreIsNotFound() {
    Integer storeId = 1;
    StoreDTO storeDTO = new StoreDTO();
    storeDTO.setName("Updated Store");

    when(storeRepository.findById(storeId)).thenReturn(Optional.empty());

    assertThrows(GeneralException.class, () -> {
      storeService.patchStore(storeId, storeDTO);
    }, "Expected IdNotFound to be thrown if store is not found");
  }

  @Test
  void patchStore_WhenErrorOccurs() {
    Integer storeId = 1;
    StoreDTO storeDTO = new StoreDTO();
    storeDTO.setName("Updated Store");

    when(storeRepository.findById(storeId)).thenThrow(new RuntimeException());

    Exception exception = assertThrows(GeneralException.class, () -> {
      storeService.patchStore(storeId, storeDTO);
    });
    String expectedMessage = "Error updating store: ";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  @DisplayName("Update Store Successfully")
  void updateStore_WhenStoreIsUpdatedSuccessfully() {
    Integer storeId = 1;
    StoreDTO storeDTO = new StoreDTO();
    storeDTO.setName("Updated Store");

    Store store = new Store();
    store.setName("Test Store");

    when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
    when(storeRepository.save(any(Store.class))).thenReturn(store);
    when(storeMapper.storeToStoreDTO(store)).thenReturn(storeDTO);

    CustomApiResponse<StoreDTO> response = storeService.updateStore(storeId, storeDTO);

    assertNotNull(response, "The response should not be null");
    assertEquals("Store updated successfully", response.getMessage(), "Unexpected response message");
    assertNotNull(response.getData(), "The store data should not be null");
    assertEquals("Updated Store", response.getData().getName(), "Store name does not match expected");

    verify(storeRepository).findById(storeId);
    verify(storeMapper).updateStoreFromDto(storeDTO, store);
    verify(storeRepository).save(any(Store.class));
    verify(storeMapper).storeToStoreDTO(store);
  }

  @Test
  @DisplayName("Update Store Throws IdNotFound When Store Is Not Found")
  void updateStore_WhenStoreIsNotFound() {
    Integer storeId = 1;
    StoreDTO storeDTO = new StoreDTO();
    storeDTO.setName("Updated Store");

    when(storeRepository.findById(storeId)).thenReturn(Optional.empty());

    assertThrows(IdNotFound.class, () -> {
      storeService.updateStore(storeId, storeDTO);
    }, "Expected IdNotFound to be thrown if store is not found");
  }

  @Test
  @DisplayName("Update Store Throws GeneralException When Error Occurs")
  void updateStore_WhenErrorOccurs() {
    Integer storeId = 1;
    StoreDTO storeDTO = new StoreDTO();
    storeDTO.setName("Updated Store");

    Store store = new Store();
    store.setName("Test Store");

    when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
    when(storeRepository.save(store)).thenThrow(new RuntimeException());

    Exception exception = assertThrows(GeneralException.class, () -> {
      storeService.updateStore(storeId, storeDTO);
    });
    String expectedMessage = "Error updating store: ";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));

    verify(storeRepository).findById(storeId);
    verify(storeMapper).updateStoreFromDto(storeDTO, store);
    verify(storeRepository).save(store);
  }


  @Test
  @DisplayName("Get Employees From Store Id Successfully")
  void getEmployeesFromStoreId_Success() {
    Integer storeId = 1;
    Employee employee = new Employee();
    employee.setFirstName("Test Employee");
    EmployeeDTO employeeDTO = new EmployeeDTO();
    employeeDTO.setFirstName("Test Employee");

    when(employeeRepository.getEmplotesByStoreId(storeId)).thenReturn(Arrays.asList(employee));
    when(employeeMapper.employeeToEmployeeDTO(employee)).thenReturn(employeeDTO);

    CustomApiResponse<List<EmployeeDTO>> response = storeService.getEmployeesFromStoreId(storeId);

    assertNotNull(response, "The response should not be null");
    assertEquals("Employees found successfully", response.getMessage(), "Unexpected response message");
    assertFalse(response.getData().isEmpty(), "The data list should not be empty");
    assertEquals("Test Employee", response.getData().get(0).getFirstName(), "Employee name does not match expected");

    verify(employeeRepository).getEmplotesByStoreId(storeId);
    verify(employeeMapper).employeeToEmployeeDTO(employee);
  }

  @Test
  @DisplayName("Get Employees From Store Id Throws GeneralException When Error Occurs")
  void getEmployeesFromStoreId_WhenErrorOccurs() {
    Integer storeId = 1;

    when(employeeRepository.getEmplotesByStoreId(storeId)).thenThrow(new RuntimeException());

    Exception exception = assertThrows(GeneralException.class, () -> {
      storeService.getEmployeesFromStoreId(storeId);
    });
    String expectedMessage = "Error finding employees for store ID: " + storeId;
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));

    verify(employeeRepository).getEmplotesByStoreId(storeId);
  }

  @Test
  @DisplayName("Get Sales From Store Id Successfully")
  void getSalesFromStoreId_Success() {

    Integer storeId = 1;
    Sale sale = Mockito.mock(Sale.class);
    SaleDTO saleDTO = Mockito.mock(SaleDTO.class);

    when(saleRepository.getByStoreId(storeId)).thenReturn(Arrays.asList(sale));
    when(saleMapper.saleToSaleDTO(sale)).thenReturn(saleDTO);

    CustomApiResponse<List<SaleDTO>> response = storeService.getSalesFromStoreId(storeId);

    assertNotNull(response, "The response should not be null");
    assertEquals("Sale found successfully", response.getMessage(), "Unexpected response message");
    assertFalse(response.getData().isEmpty(), "The data list should not be empty");
    assertThat(response.getData().get(0)).isEqualTo(saleDTO);

    verify(saleRepository).getByStoreId(storeId);
    verify(saleMapper).saleToSaleDTO(sale);
  }



  @Test
  @DisplayName("Get Sales From Store Id Throws IdNotFound When No Sales Found")
  void getSalesFromStoreId_WhenNoSalesFound() {
    Integer storeId = 1;

    when(saleRepository.getByStoreId(storeId)).thenReturn(Collections.emptyList());

    Exception exception = assertThrows(IdNotFound.class, () -> {
      storeService.getSalesFromStoreId(storeId);
    });
    String expectedMessage = "No sales found for store ID: " + storeId;
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));

    verify(saleRepository).getByStoreId(storeId);
  }


  @Test
  @DisplayName("Get Sales From Store Id Throws GeneralException When Error Occurs")
  void getSalesFromStoreId_WhenErrorOccurs() {
    Integer storeId = 1;

    when(saleRepository.getByStoreId(storeId)).thenThrow(new RuntimeException());

    Exception exception = assertThrows(GeneralException.class, () -> {
      storeService.getSalesFromStoreId(storeId);
    });
    String expectedMessage = "Error finding sales: ";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));

    verify(saleRepository).getByStoreId(storeId);
  }

  @Test
  @DisplayName("Get Total Sales By Store Successfully")
  void getTotalSalesByStore_Success() {
    Integer storeId = 1;
    Double expectedTotalSales = 1000.0;

    when(saleRepository.getTotalByStoreId(storeId)).thenReturn(Optional.of(expectedTotalSales));

    CustomApiResponse<Double> response = storeService.getTotalSalesByStore(storeId);

    assertNotNull(response, "The response should not be null");
    assertEquals("Total sales calculated successfully", response.getMessage(), "Unexpected response message");
    assertEquals(expectedTotalSales, response.getData(), "Total sales does not match expected");
  }

  @Test
  @DisplayName("Get Total Sales By Store Returns Zero When No Sales Found")
  void getTotalSalesByStore_ReturnsZeroWhenNoSalesFound() {
    Integer storeId = 1;
    Double expectedTotalSales = 0.0;

    when(saleRepository.getTotalByStoreId(storeId)).thenReturn(Optional.empty());

    CustomApiResponse<Double> response = storeService.getTotalSalesByStore(storeId);

    assertNotNull(response, "The response should not be null");
    assertEquals("Total sales calculated successfully", response.getMessage(), "Unexpected response message");
    assertEquals(expectedTotalSales, response.getData(), "Total sales does not match expected");
  }

  @Test
  @DisplayName("Get Total Sales By Store Throws GeneralException When Error Occurs")
  void getTotalSalesByStore_WhenErrorOccurs() {
    Integer storeId = 1;

    when(saleRepository.getTotalByStoreId(storeId)).thenThrow(new RuntimeException());

    Exception exception = assertThrows(GeneralException.class, () -> {
      storeService.getTotalSalesByStore(storeId);
    });
    String expectedMessage = "Error calculating total sales: ";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  @DisplayName("Get All Stores Paginated Successfully")
  void getAllStoresPag_Success() {
    Integer id = 1;
    String name = "Test Store";
    String location = "Test Location";
    Pageable pageable = PageRequest.of(0, 10);

    Store store = new Store();
    store.setId(id);
    store.setName(name);
    store.setLocation(location);

    StoreDTO storeDTO = new StoreDTO();
    storeDTO.setId(id);
    storeDTO.setName(name);

    Page<Store> storePage = new PageImpl<>(Collections.singletonList(store));

    when(storeRepository.findAll(any(StoreSpec.class), any(Pageable.class))).thenReturn(storePage);
    when(storeMapper.storeToStoreDTO(store)).thenReturn(storeDTO);

    CustomApiResponse<Page<StoreDTO>> response = storeService.getAllStoresPag(id, name, location, pageable);

    assertNotNull(response, "The response should not be null");
    assertEquals("Filtered stores retrieved successfully", response.getMessage(), "Unexpected response message");
    assertFalse(response.getData().isEmpty(), "The data list should not be empty");
    assertEquals(name, response.getData().getContent().get(0).getName(), "Store name does not match expected");

    verify(storeRepository).findAll(any(StoreSpec.class), any(Pageable.class));
    verify(storeMapper).storeToStoreDTO(store);
  }

  @Test
  @DisplayName("Get All Stores Paginated Throws GeneralException When Error Occurs")
  void getAllStoresPag_WhenErrorOccurs() {
    Integer id = 1;
    String name = "Test Store";
    String location = "Test Location";
    Pageable pageable = PageRequest.of(0, 10);

    when(storeRepository.findAll(any(StoreSpec.class), any(Pageable.class))).thenThrow(new RuntimeException());

    Exception exception = assertThrows(GeneralException.class, () -> {
      storeService.getAllStoresPag(id, name, location, pageable);
    });
    String expectedMessage = "Error finding filtered stores: ";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));

    verify(storeRepository).findAll(any(StoreSpec.class), any(Pageable.class));
  }


  @Test
  public void whenFindAllWithSpecification_thenReturnStores() {
    Store store1 = new Store();
    store1.setName("Test Store 1");

    Store store2 = new Store();
    store2.setName("Test Store 2");

    StoreSpec spec = new StoreSpec(null, "Test Store 1", null);

    Page<Store> page = new PageImpl<>(Collections.singletonList(store1));
    when(storeRepository.findAll(any(StoreSpec.class), any(PageRequest.class))).thenReturn(page);

    Page<Store> stores = storeRepository.findAll(spec, PageRequest.of(0, 10));

    assertNotNull(stores, "Stores page should not be null");
    assertEquals(1, stores.getContent().size(), "Stores page should contain exactly one store");
    assertEquals("Test Store 1", stores.getContent().get(0).getName(), "Store name should match");
  }

  @Test
  @DisplayName("Get Store Sales Successfully")
  void getStoreSales_Success() {
    Object[] storeSalesData = new Object[] {1, "Test Store", 1000.0};
    List<Object[]> results = Collections.singletonList(storeSalesData);

    when(storeRepository.findStoresTopSellers()).thenReturn(results);

    CustomApiResponse<List<StoreDTO>> response = storeService.getStoreSales();

    assertNotNull(response, "The response should not be null");
    assertEquals("Success", response.getMessage(), "Unexpected response message");
    assertFalse(response.getData().isEmpty(), "The data list should not be empty");
    assertEquals(1, response.getData().get(0).getId(), "Store ID does not match expected");
    assertEquals("Test Store", response.getData().get(0).getName(), "Store name does not match expected");
    assertEquals(1000.0, response.getData().get(0).getTotalSales(), "Total sales does not match expected");

    verify(storeRepository).findStoresTopSellers();
  }

  @Test
  @DisplayName("Get Store Sales Returns Empty List When No Sales Found")
  void getStoreSales_ReturnsEmptyListWhenNoSalesFound() {
    when(storeRepository.findStoresTopSellers()).thenReturn(Collections.emptyList());

    CustomApiResponse<List<StoreDTO>> response = storeService.getStoreSales();

    assertNotNull(response, "The response should not be null");
    assertEquals("Success", response.getMessage(), "Unexpected response message");
    assertTrue(response.getData().isEmpty(), "The data list should be empty");

    verify(storeRepository).findStoresTopSellers();
  }

}
