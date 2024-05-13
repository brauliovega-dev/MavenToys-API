package com.oreilly.maventoys.repository;

import com.oreilly.maventoys.model.entity.Store;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StoreRepositoryTest {
  private static final Integer EXPECTED_STORES_COUNT = 1;
  @Autowired

  private StoreRepository storeRepository;

  @Autowired
  private TestEntityManager entityManager;

//  @Test
//  void getByActiveTrue() {
//    Store activeStore = new Store();
//    activeStore.setName("Active Store");
//    activeStore.setActive(true);
//    entityManager.persist(activeStore);
//
//    Store inactiveStore = new Store();
//    inactiveStore.setName("Inactive Store");
//    inactiveStore.setActive(false);
//    entityManager.persist(inactiveStore);
//
//    entityManager.flush();
//
//    List<Store> result = storeRepository.getByActiveTrue();
//
//    // Verificar el resultado
//    assertFalse(result.isEmpty(), "La lista de tiendas activas no debe estar vacía.");
//    assertTrue(result.stream().allMatch(Store::getActive), "Todas las tiendas en la lista deben estar activas.");
//    assertEquals(1, result.size(), "Debe haber exactamente una tienda activa.");
//    assertEquals("Active Store", result.get(0).getName(), "El nombre de la tienda activa debe coincidir.");
//
//
//
//  }


  @Test
  @DisplayName("Should return a list of active stores")
  void getByActiveTrue() {
    Store activeStore = new Store();
    activeStore.setName("Active Store");
    activeStore.setActive(true);
    storeRepository.save(activeStore);

    Store inactiveStore = new Store();
    inactiveStore.setName("Inactive Store");
    inactiveStore.setActive(false);
    storeRepository.save(inactiveStore);

    List<Store> result = storeRepository.getByActiveTrue();

    assertFalse(result.isEmpty(), "La lista de tiendas activas no debe estar vacía.");
    assertTrue(result.stream().allMatch(Store::getActive), "Todas las tiendas en la lista deben estar activas.");
    Assertions.assertThat(result.size()).isEqualTo(EXPECTED_STORES_COUNT);
    assertEquals(EXPECTED_STORES_COUNT, result.size(), "Debe haber exactamente una tienda activa.");
    assertEquals("Active Store", result.get(0).getName(), "El nombre de la tienda activa debe coincidir.");
  }

  @Test
  @DisplayName("Should return empty list when all stores are inactive")
  void shouldReturnEmptyWhenAllStoresAreInactive() {
    Store inactiveStore1 = new Store();
    inactiveStore1.setName("Inactive Store 1");
    inactiveStore1.setActive(false);
    storeRepository.save(inactiveStore1);

    Store inactiveStore2 = new Store();
    inactiveStore2.setName("Inactive Store 2");
    inactiveStore2.setActive(false);
    storeRepository.save(inactiveStore2);

    List<Store> result = storeRepository.getByActiveTrue();
    Assertions.assertThat(result).isEmpty();
    //assertTrue(result.isEmpty(), "The list of active stores should be empty tontuelo.");
  }
}