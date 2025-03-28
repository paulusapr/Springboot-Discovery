package com.example.product.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.product.model.Product;
import com.example.product.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

  @Mock
  private ProductRepository productRepository;

  @InjectMocks
  private ProductService productService;

  private Product product1;
  private Product product2;
  private LocalDateTime NOW;
  private Product existingProduct;
  private Product updatedProductData;

  @BeforeEach
  void setUp() {
    NOW = LocalDateTime.now();
    product1 = new Product(1L, "Product 1", 100.0);
    product2 = new Product(2L, "Product 2", 200.0);
    existingProduct = new Product(1L, "Product 1", 100.0);
    updatedProductData = new Product(1L, "Updated Product", 150.0);
  }

  @Test
  void testGetAllProducts() {
    when(productRepository.findAll()).thenReturn(List.of(
        new Product(1, "Product 1", 100.0),
        new Product(2, "Product 2", 200.0)));

    List<Product> products = productService.getAllProducts();
    assertNotNull(products);
    assertEquals(2, products.size());
    assertEquals("Product 1", products.get(0).getName());
    assertEquals("Product 2", products.get(1).getName());
    assertEquals(1, products.get(0).getId());
    assertEquals(2, products.get(1).getId());
    assertEquals(100.0, products.get(0).getPrice());
    assertEquals(200.0, products.get(1).getPrice());
  }

  @Test
  void testFindById_ProductFoundAndNotDeleted() {
    when(productRepository.findAll()).thenReturn(List.of(product1, product2));
    Optional<Product> result = productService.findById(1L);
    assertTrue(result.isPresent());
    assertEquals("Product 1", result.get().getName());
    assertFalse(result.get().isDeleted());
  }

  @Test
  void testFindById_ProductFoundButDeleted() {
    product2.setDeletedAt(NOW);
    when(productRepository.findAll()).thenReturn(List.of(product1, product2));
    Optional<Product> result = productService.findById(2L);
    assertFalse(result.isPresent());
  }

  @Test
  void testFindById_ProductNotFound() {
    when(productRepository.findAll()).thenReturn(List.of(product1, product2));
    Optional<Product> result = productService.findById(3L);
    assertFalse(result.isPresent());
  }

  @Test
  void testSaveProduct() {
    when(productRepository.save(product1)).thenReturn(product1);
    Product savedProduct = productService.save(product1);
    assertNotNull(savedProduct);
    assertEquals(product1.getId(), savedProduct.getId());
    assertEquals(product1.getName(), savedProduct.getName());
    assertEquals(product1.getPrice(), savedProduct.getPrice());
    verify(productRepository, times(1)).save(product1);
  }

  @Test
  void testUpdateProduct_ProductExists() {
    when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
    when(productRepository.save(existingProduct)).thenReturn(existingProduct);
    Product updatedProduct = productService.updateProduct(1L, updatedProductData);
    assertNotNull(updatedProduct);
    assertEquals("Updated Product", updatedProduct.getName());
    assertEquals(150.0, updatedProduct.getPrice());
    verify(productRepository, times(1)).findById(1L);
    verify(productRepository, times(1)).save(existingProduct);
  }

  @Test
  void testUpdateProduct_ProductNotFound() {
    when(productRepository.findById(1L)).thenReturn(Optional.empty());
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      productService.updateProduct(1L, updatedProductData);
    });
    assertEquals("Product not found", exception.getMessage());
    verify(productRepository, times(1)).findById(1L);
    verify(productRepository, times(0)).save(any());
  }

  @Test
  void testSoftDelete_ProductExists() {
    when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
    productService.softDelete(1L);
    assertNotNull(product1.getDeletedAt());
    assertTrue(product1.getDeletedAt().isBefore(LocalDateTime.now()));
    verify(productRepository, times(1)).save(product1);
  }

  @Test
  void testSoftDelete_ProductNotFound() {
    when(productRepository.findById(1L)).thenReturn(Optional.empty());
    productService.softDelete(1L);
    verify(productRepository, times(0)).save(any());
  }

  @Test
  void testHardDelete_ProductExists() {
    productService.hardDelete(1L);
    verify(productRepository, times(1)).deleteById(1L);
  }

  @Test
  void testHardDelete_ProductNotFound() {
    doNothing().when(productRepository).deleteById(1L);
    productService.hardDelete(1L);
    verify(productRepository, times(1)).deleteById(1L);
  }
}