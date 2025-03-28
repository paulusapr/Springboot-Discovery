package com.example.product.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.example.product.model.Product;
import com.example.product.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

  private MockMvc mockMvc;

  @Mock
  private ProductService productService;

  @InjectMocks
  private ProductController productController;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
  }

  @Test
  void testGetAllProducts() throws Exception {
    List<Product> mockProducts = List.of(
        new Product(1, "test 1", 1.55),
        new Product(2, "test 2", 90.2));

    when(productService.getAllProducts()).thenReturn(mockProducts);

    mockMvc.perform(get("/api/products"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.length()").value(2))
        .andExpect(jsonPath("$.data[0].id").value(1))
        .andExpect(jsonPath("$.data[0].name").value("test 1"))
        .andExpect(jsonPath("$.data[1].id").value(2))
        .andExpect(jsonPath("$.data[1].name").value("test 2"))
        .andExpect(jsonPath("$.meta.status").value("OK"))
        .andExpect(jsonPath("$.meta.message").value("Success"));
    // .andDo(print());

    verify(productService, times(1)).getAllProducts();
  }

  @Test
  void testGetProductById() throws Exception {
    long productId = 1;
    Product mockProduct = new Product(productId, "Test Product", 100.0);

    when(productService.findById(productId)).thenReturn(Optional.of(mockProduct));

    mockMvc.perform(get("/api/products/{id}", productId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.id").value(productId))
        .andExpect(jsonPath("$.data.name").value("Test Product"))
        .andExpect(jsonPath("$.data.price").value(100.0))
        .andExpect(jsonPath("$.meta.status").value("OK"))
        .andExpect(jsonPath("$.meta.message").value("Success"));

    verify(productService, times(1)).findById(productId);
  }

  @Test
  void testGetProductByIdNotFound() throws Exception {
    long productId = 999L;
    when(productService.findById(productId)).thenReturn(Optional.empty());

    mockMvc.perform(get("/api/products/{id}", productId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.meta.status").value("NOT_FOUND"))
        .andExpect(jsonPath("$.meta.message").value("Product Not Found"))
        .andExpect(jsonPath("$.error.type").value("not_found"))
        .andExpect(jsonPath("$.error.title").value("Product Not Found"))
        .andExpect(jsonPath("$.error.detail").value("No product found with id 999"))
        .andExpect(jsonPath("$.error.status").value("NOT_FOUND"));

    verify(productService, times(1)).findById(productId);
  }

  @Test
  void testCreateProduct() throws Exception {
    Product product = new Product(1, "New Product", 199.99);
    when(productService.save(any(Product.class))).thenReturn(product);

    mockMvc.perform(post("/api/products")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(product)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data.id").value(1))
        .andExpect(jsonPath("$.data.name").value("New Product"))
        .andExpect(jsonPath("$.data.price").value(199.99))
        .andExpect(jsonPath("$.meta.status").value("CREATED"))
        .andExpect(jsonPath("$.meta.message").value("Product Created"));

    verify(productService, times(1)).save(any(Product.class));
  }

  @Test
  void testUpdateProduct() throws Exception {
    Long productId = 1L;
    Product existingProduct = new Product(productId, "Old Product", 100.0);
    Product updatedProductData = new Product(productId, "Updated Product", 150.0);
    Product updatedProduct = new Product(productId, "Updated Product", 150.0);

    when(productService.findById(productId)).thenReturn(Optional.of(existingProduct));
    when(productService.updateProduct(anyLong(), any(Product.class))).thenReturn(updatedProduct);

    mockMvc.perform(put("/api/products/{id}", productId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(updatedProductData)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.id").value(productId))
        .andExpect(jsonPath("$.data.name").value("Updated Product"))
        .andExpect(jsonPath("$.data.price").value(150.0))
        .andExpect(jsonPath("$.meta.status").value("OK"))
        .andExpect(jsonPath("$.meta.message").value("Updated"));

    verify(productService, times(1)).findById(productId);
    verify(productService, times(1)).updateProduct(anyLong(), any(Product.class));
  }

  @Test
  void testUpdateProductNotFound() throws Exception {
    Long productId = 999L;
    Product updatedProductData = new Product(productId, "Updated Product", 150.0);

    when(productService.findById(productId)).thenReturn(Optional.empty());

    mockMvc.perform(put("/api/products/{id}", productId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(updatedProductData)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.meta.status").value("NOT_FOUND"))
        .andExpect(jsonPath("$.meta.message").value("Product Not Found"))
        .andExpect(jsonPath("$.error.type").value("not_found"))
        .andExpect(jsonPath("$.error.title").value("Product Not Found"))
        .andExpect(jsonPath("$.error.detail").value("No product found with id 999"))
        .andExpect(jsonPath("$.error.status").value("NOT_FOUND"));

    verify(productService, times(1)).findById(productId);
    verify(productService, never()).updateProduct(anyLong(), any(Product.class));
  }

  @Test
  void testDeleteProduct_Success() throws Exception {
    Long productId = 1L;
    Product existingProduct = new Product(productId, "Test Product", 100.0);

    when(productService.findById(productId)).thenReturn(Optional.of(existingProduct));

    doNothing().when(productService).softDelete(productId);

    mockMvc.perform(delete("/api/products/{id}", productId))
        .andExpect(status().isNoContent())
        .andExpect(content().string(""));

    verify(productService, times(1)).findById(productId);
    verify(productService, times(1)).softDelete(productId);
  }

  @Test
  void testDeleteProduct_NotFound() throws Exception {
    Long productId = 999L;
    when(productService.findById(productId)).thenReturn(Optional.empty());

    mockMvc.perform(delete("/api/products/{id}", productId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.meta.status").value("NOT_FOUND"))
        .andExpect(jsonPath("$.meta.message").value("Product Not Found"))
        .andExpect(jsonPath("$.error.type").value("not_found"))
        .andExpect(jsonPath("$.error.title").value("Product Not Found"))
        .andExpect(jsonPath("$.error.detail").value("No product found with id 999"))
        .andExpect(jsonPath("$.error.status").value("NOT_FOUND"));

    verify(productService, times(1)).findById(productId);
    verify(productService, never()).softDelete(anyLong());
  }

}
