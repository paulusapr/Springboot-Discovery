# 2. Design Pattern

In Spring Boot, the Spring MVC framework implements this pattern, and it is commonly used to build web applications. Below is an overview of the MVC pattern and how it is typically structured in a Spring Boot application:

## 2.1. MVC

Spring Boot MVC (Model-View-Controller) is a framework for building web applications following the MVC design pattern. It simplifies web application development by providing built-in functionalities such as dependency injection, auto-configuration, and embedded web servers.

### Understanding MVC Architecture

MVC (Model-View-Controller) is a design pattern used to separate concerns in an application:

- **Model**: Represents the data and business logic.
- **View**: Displays the data to the user.
- **Controller**: Handles user input and communicates between the model and view.

### How Spring Boot Implements MVC:

- **Model**: Java classes that represent data and use JPA/Hibernate for persistence.
- **View**: Typically Thymeleaf, JSP, or frontend frameworks like React/Angular.
- **Controller**: Java classes annotated with `@Controller` or `@RestController`.

---

## 2.2. How To Create a Component

Spring Boot provides several ways to create reusable components using annotations:

### **2.2.1. Using @Component**

The `@Component` annotation marks a class as a Spring-managed component.

```java
package com.example.product.component;

import com.example.product.model.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductComponent {

  private final List<Product> productList = new ArrayList<>();

  public ProductComponent() {
    productList.add(new Product(1, "Laptop", 1500.00));
    productList.add(new Product(2, "Smartphone", 800.00));
    productList.add(new Product(3, "Tablet", 500.00));
  }
}
```

### **2.2.2. Using @Service**

The `@Service` annotation is a specialization of `@Component` for service layer logic.

```java
package com.example.product.service;

import com.example.product.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.product.repository.ProductRepository;

// if you want to use ProductComponent
// import com.example.product.component.ProductComponent;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    // if you want to use ProductComponent
    // private final ProductComponent productComponent;
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
}
```

### **2.2.3. Using @Repository**

The `@Repository` annotation is a specialization of `@Component` for the persistence layer.

```java
package com.example.mvc.repository;

import com.example.product.model.Product;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {
    private List<Product> products = new ArrayList<>();

    public ProductRepository() {
        products.add(new Product(1, "Laptop", 1200.00));
        products.add(new Product(2, "Smartphone", 800.00));
    }

    public List<Product> findAll() {
        return products;
    }

    public Optional<Product> findById(long id) {
        return products.stream().filter(p -> p.getId() == id).findFirst();
    }

    public void save(Product product) {
        products.add(product);
    }
}
```

### **2.2.4. Using @Controller**

The `@Controller` example `@RestController` annotation marks a class as a web controller in `src/main/java/com/example/product/Controller/ProductController.java`

```java
package com.example.product.controller;

import com.example.product.model.Product;
import com.example.product.service.ProductService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }
}
```

---

## 2.3. Creating the Model

Create a simple `Product` model in `src/main/java/com/example/product/model/Product.java`:

```java
package com.example.product.model;

public class Product {
    private long id;
    private String name;
    private double price;

    // Constructor, getters, and setters
    public Product(long id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
```

---

## 2.4. Creating the View

Create `user.html` inside `src/main/resources/templates/`:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
  <head>
    <link rel="stylesheet" th:href="@{/css/product.css}" />
    <title>Product List</title>
  </head>
  <body>
    <h1>Product List</h1>
    <table class="table table-striped table-bordered">
      <thead class="table-dark">
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>Price</th>
        </tr>
      </thead>
      <tbody>
        <tr th:each="product : ${products}">
          <td th:text="${product.id}"></td>
          <td th:text="${product.name}"></td>
          <td th:text="${product.price}"></td>
        </tr>
      </tbody>
    </table>
  </body>
</html>
```

---

## 2.5. Creating the Controller

Create `ProductController` in `src/main/java/com/example/product/Controller/ProductController.java`:

```java
package com.example.product.controller;

import com.example.product.model.Product;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
  private List<Product> products = new ArrayList<>();

    // Constructor to add some sample products
    public ProductController() {
        products.add(new Product(1, "Laptop", 799.99));
        products.add(new Product(2, "Smartphone", 499.99));
        products.add(new Product(3, "Tablet", 299.99));
    }

    // Get all products
    @GetMapping
    public List<Product> getAllProducts() {
        return products;
    }

    // Get a single product by ID
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable long id) {
        return products.stream()
                .filter(product -> product.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // Create a new product
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        products.add(product);
        return product;
    }

    // Update an existing product
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable long id, @RequestBody Product updatedProduct) {
        Product product = getProductById(id);
        if (product != null) {
            product.setName(updatedProduct.getName());
            product.setPrice(updatedProduct.getPrice());
            return product;
        }
        return null;
    }

    // Delete a product
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable long id) {
        Product product = getProductById(id);
        if (product != null) {
            products.remove(product);
            return "Product deleted";
        }
        return "Product not found";
    }
}

```

---

## 2.6. Creating the Service

Create `ProductService` in `src/main/java/com/example/product/service/ProductService.java`:

```java
package com.example.product.service;

import com.example.product.model.Product;
import com.example.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(long id) {
        return productRepository.findById(id);
    }

    public void addProduct(Product product) {
        productRepository.save(product);
    }
}
```

---

## Project Structure Best Practices

### **1. Standard MVC Project Structure**

```
my-springboot-app/
│-- src/
│   ├── main/java/com/example/app/
│   │   ├── controller/
│   │   │   ├── UserController.java
│   │   ├── model/
│   │   │   ├── User.java
│   │   ├── service/
│   │   │   ├── UserService.java
│   │   ├── repository/
│   │   │   ├── UserRepository.java
│   │   ├── MySpringBootApplication.java
│   ├── resources/
│   │   ├── templates/
│   │   │   ├── user.html
│   │   │   ├── register.html
│   │   ├── application.properties
│-- pom.xml
```

✅ **Good for small to medium projects**

### **2. Feature-Based Structure**

```
my-springboot-app/
│-- src/main/java/com/example/app/
│   ├── user/
│   │   ├── controller/UserController.java
│   │   ├── service/UserService.java
│   │   ├── repository/UserRepository.java
│   │   ├── model/User.java
│   ├── order/
│   │   ├── controller/OrderController.java
│   │   ├── service/OrderService.java
│   │   ├── repository/OrderRepository.java
│   │   ├── model/Order.java
│-- pom.xml
```

✅ **Good for large applications**

### **3. Springboot-POC Project Structure**

```
src/
├── main/java/com/example/product/
│   ├── component/
│   ├── controller/
│   │   ├── ProductController.java
│   │   ├── ProductPageController.java
│   ├── model/
│   │   ├── Product.java
│   ├── service/
│   │   ├── ProductService.java
│   ├── repository/
│   │   ├── ProductRepository.java
│   ├── dto/
│   │   ├── ApiResponse.java
│   │   ├── ApiErrorResponse.java
│   ├── ProductApp.java
├── main/resources/templates/
│   ├── product.html
│   ├── error/
│   │   ├── 404.html
│   │   ├── 500.html
├── main/resources/static/css/
│   ├── style.css
├── main/resources/db/migration/
│   ├── V1__create_products_table.sql
├── main/resources/application.properties
```

✅ **Good for small to medium projects**

## Running the Application

1. Navigate to the project directory.
2. Run the command:
   ```
    mvn spring-boot:run
   ```
3. Open your browser and access:

**Full URL Endpoints**

- Get All Products: **GET** `http://localhost:8080/api/products`
- Get Product By ID: **GET** `http://localhost:8080/api/products/{id}`
- Add Product: **POST** `http://localhost:8080/api/products`

**API Response Format**

```json
// Success Response:
{
  "meta": {
    "code": 200,
    "message": "Success",
    "date": "2025-03-24T10:00:00Z"
  },
  "data": {...},
  "error": []
}

// Error Response:
{
  "meta": {
    "code": 400,
    "message": "Bad Request",
    "date": "2025-03-24T10:00:00Z"
  },
  "data": null,
  "error": ["Invalid product ID"]
}
```

---

## Conclusion

This guide provides a hands-on tutorial for Spring Boot MVC, covering setup, project structure, and form handling. You can extend it with Spring Data JPA and Spring Security for a full-fledged application.
