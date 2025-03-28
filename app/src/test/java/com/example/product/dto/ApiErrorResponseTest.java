package com.example.product.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import java.time.Instant;

class ApiErrorResponseTest {

  @Test
  void testApiErrorResponseConstructorAndGetters() {
    // Arrange
    String type = "not_found";
    String title = "Product Not Found";
    String detail = "No product found with the given ID";
    HttpStatus status = HttpStatus.NOT_FOUND;

    // Act
    ApiErrorResponse errorResponse = new ApiErrorResponse(type, title, detail, status);

    // Assert
    assertNotNull(errorResponse);
    assertEquals(type, errorResponse.getType());
    assertEquals(title, errorResponse.getTitle());
    assertEquals(detail, errorResponse.getDetail());
    assertEquals(status, errorResponse.getStatus());
    assertNotNull(errorResponse.getTimestamp());
    assertTrue(errorResponse.getTimestamp().isBefore(Instant.now()));
  }
}
