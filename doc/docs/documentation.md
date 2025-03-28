# 6. Documentation

## 6.1. Prerequisite Swagger

Swagger is an API documentation tool that allows developers to interact with and test APIs using a user-friendly interface. Before implementing Swagger in a Spring Boot project, ensure the following prerequisites are met:

### **Prerequisites:**

- **Spring Boot Application**: A running Spring Boot project with REST APIs.
- **Maven or Gradle**: Dependency management tool installed.
- **Springfox or OpenAPI**: Swagger implementation libraries.
- **Java 11+**: Recommended for compatibility and performance.

## 6.2. How to Implement Swagger

To integrate Swagger into a Spring Boot project, follow these steps:

### **Step 1: Add Swagger Dependencies**

For **Springfox Swagger**, add the following dependencies to `pom.xml`:

```xml
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-boot-starter</artifactId>
    <version>3.0.0</version>
</dependency>
```

Or for **Springdoc OpenAPI**:

```xml
<dependency>
  <groupId>org.springdoc</groupId>
  <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
  <version>2.3.0</version>
</dependency>
```

### **Step 2: Enable Swagger in Spring Boot**

Create a Swagger configuration class in `com.example.config`:

```java
package com.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Product API")
            .version("1.0")
            .description("API documentation for managing products"));
  }
}
```

### **Step 3: Annotate API Endpoints**

To document your APIs, use Swagger annotations in the controller:

```java
package com.example.product.controller;

import com.example.product.model.Product;
import com.example.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product Controller", description = "Manage products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieve a list of all available products")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }
}
```

### **Step 4: Enable OpenAPI Documentation**

Ensure the following properties exist in `src/main/resources/application.properties`:

```java
springdoc.api-docs.enabled=true
springdoc.swagger-ui.path=/swagger-ui.html
server.error.include-message=always
```

### **Step 5: Access Swagger UI**

After starting your Spring Boot application, access the Swagger UI at:

- **Swagger UI**: `http://localhost:8080/swagger-ui/index.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

## 6.3. Troubleshooting

**Issue: 404 Not Found on `/v3/api-docs`**

If `/v3/api-docs` returns `404`, check:

1. Ensure the Springdoc dependency is included in `pom.xml`.
2. Run `mvn clean install` to recompile dependencies.
3. Ensure the application is running properly without errors.
4. Verify that the `SwaggerConfig.java` class is inside a scanned package.

**Issue: Swagger UI Shows Empty List**

If the Swagger UI loads but doesn’t display your endpoints:

1. Ensure your controller methods are annotated with `@Operation(summary = "...")`.
2. Verify that `@RequestMapping("/api/products")` is correctly used in `ProductController`.
3. Restart the application after making changes.

This completes the setup of Swagger in your Spring Boot application for API documentation and testing.
