# 3. Development API
A REST API (Representational State Transfer Application Programming Interface) is a set of rules and conventions used for building and interacting with web services over HTTP. RESTful APIs are widely used to allow different software applications to communicate with each other, typically over the internet.

A RESTful API has a consistent and standardized way of interacting with the resources, typically using HTTP methods such as:
GET: Retrieve data from the server.
POST: Send data to the server (often to create a new resource).
PUT: Update an existing resource on the server.
DELETE: Remove a resource from the server.

## 3.1. Created Method Get
the GET method is used to retrieve or fetch data from a server. It is one of the most commonly used HTTP methods and is typically used when the client wants to read information from a specific resource or a collection of resources.

This example code is in Spring Boot. For GET requests, we need the annotation @GetMapping.
```java
// Get all products
 @GetMapping
public List<Product> getAllProducts() {
    return products;
}
```
## 3.2. Created Method Post
The POST method in a REST API is used to send data to a server, typically to create a new resource or submit data for processing. It is one of the HTTP methods used in RESTful APIs for communication between clients and servers.

This example code is in Spring Boot. For POST requests, we need the annotation @PostMapping.
```java
// Create a new product
@PostMapping
public Product createProduct(@RequestBody Product product) {
    products.add(product);
    return product;
}
```
## 3.3. Created Method Put
The PUT method in a REST API is used to update an existing resource on the server or to create a resource if it does not already exist. PUT requests are typically used when you want to send data to the server and replace the current representation of a resource with the new data provided.

This example code is in Spring Boot. For PUT requests, we need the annotation @PutMapping.
```Java
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
```
## 3.4. Created Method Delete
The DELETE method in a REST API is used to remove a resource from the server. When a client sends a DELETE request, it instructs the server to delete the resource identified by the URL.

This example code is in Spring Boot. For DELETE requests, we need the annotation @DeleteMapping.
```java
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
```

