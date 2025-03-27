package com.example.product.dto;

import java.time.Instant;

public class ApiResponse<T> {
  private Meta meta;
  private T data;
  private Object error;

  public ApiResponse(int code, String message, T data, Object error) {
    this.meta = new Meta(code, message, Instant.now());
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

  private static class Meta {
    private int code;
    private String message;
    private Instant timestamp;

    public Meta(int code, String message, Instant timestamp) {
      this.code = code;
      this.message = message;
      this.timestamp = timestamp;
    }

    public int getCode() {
      return code;
    }

    public String getMessage() {
      return message;
    }

    public Instant getTimestamp() {
      return timestamp;
    }
  }
}
