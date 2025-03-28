package com.example.product.dto;

import java.time.Instant;

import org.springframework.http.HttpStatus;

public class ApiResponse<T> {
  private Meta meta;
  private T data;
  private Object error;

  public ApiResponse(HttpStatus status, String message, T data, Object error) {
    this.meta = new Meta(status, message, Instant.now());
    this.data = data;
    this.error = error == null ? new Object[] {} : error;
  }

  public Meta getMeta() {
    return meta;
  }

  public T getData() {
    return data;
  }

  public Object getError() {
    return error;
  }

  static class Meta {
    private HttpStatus status;
    private String message;
    private Instant timestamp;

    public Meta(HttpStatus status, String message, Instant timestamp) {
      this.status = status;
      this.message = message;
      this.timestamp = timestamp;
    }

    public HttpStatus getstatus() {
      return status;
    }

    public String getMessage() {
      return message;
    }

    public Instant getTimestamp() {
      return timestamp;
    }
  }
}
