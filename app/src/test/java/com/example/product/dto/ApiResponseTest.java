package com.example.product.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ApiResponseTest {

  @Test
  void testApiResponseConstructorAndGetters() {
    HttpStatus status = HttpStatus.OK;
    String message = "Success";
    String data = "Test Data";
    Object error = null;

    // Creating ApiResponse instance using the constructor
    ApiResponse<String> response = new ApiResponse<>(status, message, data, error);

    // Validating the values in the ApiResponse instance
    assertNotNull(response);
    assertEquals(status, response.getMeta().getstatus());
    assertEquals(message, response.getMeta().getMessage());
    assertNotNull(response.getMeta().getTimestamp()); // Verifying timestamp is set
    assertEquals(data, response.getData());
    assertNotNull(response.getError()); // Verifying error is not null
    assertArrayEquals(new Object[] {}, (Object[]) response.getError()); // Ensuring error is empty array
  }

  @Test
  void testApiResponseConstructorWithError() {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    String message = "Failure";
    String data = "Test Data";
    Object error = new String[] { "Error details" };

    // Creating ApiResponse instance with an error
    ApiResponse<String> response = new ApiResponse<>(status, message, data, error);

    // Validating the values in the ApiResponse instance
    assertNotNull(response);
    assertEquals(status, response.getMeta().getstatus());
    assertEquals(message, response.getMeta().getMessage());
    assertNotNull(response.getMeta().getTimestamp()); // Verifying timestamp is set
    assertEquals(data, response.getData());
    assertNotNull(response.getError());
    assertArrayEquals(new String[] { "Error details" }, (String[]) response.getError()); // Verifying the error details
  }
}
