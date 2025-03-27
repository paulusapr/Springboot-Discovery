package com.example.product.dto;

import java.time.Instant;

public class ApiErrorResponse {
  private String type;
  private String title;
  private String detail;
  private int status;
  private Instant timestamp;

  public ApiErrorResponse(String type, String title, String detail, int status) {
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

  public int getStatus() {
    return status;
  }

  public Instant getTimestamp() {
    return timestamp;
  }
}
