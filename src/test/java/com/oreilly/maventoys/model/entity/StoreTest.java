package com.oreilly.maventoys.model.entity;

import com.oreilly.maventoys.repository.StoreRepository;
import com.oreilly.maventoys.service.StoreService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StoreTest {
  @Mock
  private StoreRepository storeRepository;

  @InjectMocks
  private StoreService storeService;

  @Test
  void testStoreCreationAndGettersSetters() {
    Store store = new Store();
    store.setName("Test Store");
    store.setCity("Test City");
    store.setLocation("Test Location");
    store.setOpenDate(LocalDate.now());
    store.setActive(true);

    assertEquals("Test Store", store.getName());
    assertEquals("Test City", store.getCity());
    assertEquals("Test Location", store.getLocation());
    assertEquals(LocalDate.now(), store.getOpenDate());
    assertEquals(true, store.getActive());
  }

}