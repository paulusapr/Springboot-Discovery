package com.example.product.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.product.model.Product;
import com.example.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class PageControllerTest {

  @Mock
  private ProductService productService;

  @Mock
  private Model model;

  @InjectMocks
  private PageController pageController;

  @BeforeEach
  void setUp() {
  }

  @Test
  void testShowProducts() {
    // Arrange
    when(productService.getAllProducts()).thenReturn(List.of(
        new Product(1, "Product 1", 100.0),
        new Product(2, "Product 2", 200.0)));

    // Act
    String viewName = pageController.showProducts(model);

    // Assert
    assertEquals("product", viewName);
    verify(model, times(1)).addAttribute("products", productService.getAllProducts());
  }

  @Test
  void testErrorTest() {
    // Act & Assert: verify that the exception is thrown
    assertThrows(RuntimeException.class, () -> {
      pageController.errorTest();
    });
  }
}
