# 5. Unit Test

Unit testing is crucial for verifying that individual components of your Spring Boot application work as expected.

## 5.1. How to Setup Unit Test

#### Step 1: Add Dependencies

Ensure you have the necessary dependencies in `pom.xml` (if not already present):

```xml
<!-- Spring Boot Starter Test (Includes JUnit, Mockito, Spring Test) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
    <exclusions>
        <exclusion>
            <groupId>org.junit.vintage</groupId>
            <artifactId>junit-vintage-engine</artifactId>
        </exclusion>
    </exclusions>
</dependency>

<!-- Mockito Core -->
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <scope>test</scope>
</dependency>

<!-- Mockito JUnit 5 Integration -->
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>

<!-- JUnit 5 (Jupiter) -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-engine</artifactId>
    <scope>test</scope>
</dependency>
```

#### Step 2: Create a `test` Directory

Your unit tests should be inside `src/test/java/com/example/product/`.

---

## 5.2. How to Implement Unit Test

#### Unit Test for `ProductService`

Create a new test class:

**`ProductServiceTest.java`** (inside `src/test/java/com/example/product/service/`)

```java
package com.example.product.service;

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
}
```

---

#### Unit Test for `ProductController`

**`ProductControllerTest.java`** (inside `src/test/java/com/example/product/controller/`)

```java
package com.example.product.controller;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

  private MockMvc mockMvc;

  @Mock
  private ProductService productService;

  @InjectMocks
  private ProductController productController;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
  }

  @Test
  void testGetAllProducts() throws Exception {
    List<Product> mockProducts = List.of(
        new Product(1, "test 1", 1.55),
        new Product(2, "test 2", 90.2));

    when(productService.getAllProducts()).thenReturn(mockProducts);

    mockMvc.perform(get("/api/products"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.length()").value(2))
        .andExpect(jsonPath("$.data[0].id").value(1))
        .andExpect(jsonPath("$.data[0].name").value("test 1"))
        .andExpect(jsonPath("$.data[1].id").value(2))
        .andExpect(jsonPath("$.data[1].name").value("test 2"))
        .andExpect(jsonPath("$.meta.status").value("OK"))
        .andExpect(jsonPath("$.meta.message").value("Success"));

    verify(productService, times(1)).getAllProducts();
  }
}
```

---

## 5.3. How to Run Unit Test

#### Run All Tests

To execute all unit tests, run:

```sh
mvn test
```

#### Run a Specific Test Class

To run only `ProductServiceTest`:

```sh
mvn -Dtest=ProductServiceTest test
```

#### Run a Specific Test Method

To run a single test method:

```sh
mvn -Dtest=ProductServiceTest#shouldReturnAllProducts test
```

---

### Summary

1. **Setup**: Add `spring-boot-starter-test` and `mockito-core` dependencies.
2. **Write Unit Tests**:
   - Service layer: Use `@Mock` and `@InjectMocks` to test `ProductService`.
   - Controller layer: Use `MockMvc` for HTTP request validation.
3. **Run Tests**:
   - Use `mvn test` to run all tests.
   - Use `mvn -Dtest=TestClassName test` for specific tests.
