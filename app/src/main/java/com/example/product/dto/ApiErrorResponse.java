package com.example.product.dto;

import java.time.Instant;

import org.springframework.http.HttpStatus;

public class ApiErrorResponse {
  private String type;
  private String title;
  private String detail;
  private HttpStatus status;
  private Instant timestamp;

  public ApiErrorResponse(String type, String title, String detail, HttpStatus status) {
    this.type = type;
    this.title = title;
    this.detail = detail;
    this.status = status;
    this.timestamp = Instant.now();
  }

  public String getType() {
    return type;
  }

  public String getTitle() {
    return title;
  }

  public String getDetail() {
    return detail;
  }

  public HttpStatus getStatus() {
    return status;
  }

  public Instant getTimestamp() {
    return timestamp;
  }
}
