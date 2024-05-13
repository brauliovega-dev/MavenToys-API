package com.oreilly.maventoys.controller;

import com.oreilly.maventoys.exceptions.IdNotFound;
import com.oreilly.maventoys.model.CustomApiResponse;
import com.oreilly.maventoys.model.DTO.StoreDTO;
import com.oreilly.maventoys.model.entity.Store;
import com.oreilly.maventoys.service.StoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(StoreController.class)
class StoreControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private  StoreController storeController;

  @MockBean
  private StoreService storeService;


  @Test
  void getStores() throws Exception {
    StoreDTO storeDTO = new StoreDTO();
    storeDTO.setActive(true);
    when(storeService.getStores()).thenReturn(new CustomApiResponse<>("Success", Collections.singletonList(storeDTO)));

    mockMvc.perform(get("/stores")
                        .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(content().json("{\"message\":\"Success\",\"data\":[{\"active\":true}]}"));
  }
  @Test
  void getStoreById_Success() throws Exception {
    StoreDTO storeDTO = new StoreDTO();
    storeDTO.setActive(true);
    when(storeService.getStoreById(1)).thenReturn(new CustomApiResponse<>("Success", storeDTO));

    mockMvc.perform(get("/stores/1")
                        .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(content().json("{\"message\":\"Success\",\"data\":{\"active\":true}}"));
  }

  @Test
  void getStoreById_WhenIdDoesNotExist() throws Exception {
    when(storeService.getStoreById(999)).thenThrow(new IdNotFound("Store not found"));

    mockMvc.perform(get("/stores/999")
                        .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isNotFound());
  }

  @Test
  void getStoreById_WhenIdIsInvalid() throws Exception {
    mockMvc.perform(get("/stores/abc")
                        .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isBadRequest());
  }
  @Test
  void createStore_Success() throws Exception {
    StoreDTO storeDTO = new StoreDTO();
    storeDTO.setActive(true);
    when(storeService.createStore(any(StoreDTO.class))).thenReturn(new CustomApiResponse<>("Success", storeDTO));

    mockMvc.perform(post("/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"active\":true}"))
           .andExpect(status().isCreated())
           .andExpect(content().json("{\"message\":\"Success\",\"data\":{\"active\":true}}"));
  }

  @Test
  void createStore_WhenStoreDTOIsNull() throws Exception {
    when(storeService.createStore(null)).thenThrow(new IllegalArgumentException("StoreDTO cannot be null"));

    mockMvc.perform(post("/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
           .andExpect(status().isBadRequest());
  }

  @Test
  void patchStore_Success() {
    StoreDTO storeDTO = new StoreDTO();
    storeDTO.setActive(true);
    when(storeService.patchStore(anyInt(), any(StoreDTO.class))).thenReturn(new CustomApiResponse<>("Success", storeDTO));

    ResponseEntity<CustomApiResponse<StoreDTO>> response = storeController.patchStore(1, storeDTO);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Success", response.getBody().getMessage());
    assertEquals(storeDTO, response.getBody().getData());
  }

  @Test
  void patchStore_WhenStoreDTOIsInvalid_ShouldReturnBadRequest() {
    StoreDTO storeDTO = new StoreDTO();
    storeDTO.setActive(true);
    when(storeService.patchStore(anyInt(), any(StoreDTO.class))).thenThrow(new IllegalArgumentException("Invalid StoreDTO"));

    assertThrows(IllegalArgumentException.class, () -> {
      storeController.patchStore(1, storeDTO);
    });
  }

}

