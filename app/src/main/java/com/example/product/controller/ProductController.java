package com.example.product.controller;

import com.example.product.dto.ApiResponse;
import com.example.product.dto.ApiErrorResponse;
import com.example.product.model.Product;
import com.example.product.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product Controller", description = "Manage product operations")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieve all available products")
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(HttpStatus.OK, "Success", products, null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieve a product by its ID")
    public ResponseEntity<?> getProductById(@PathVariable long id) {
        Optional<Product> product = productService.findById(id);
        if (product.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK, "Success", product.get(), null));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(HttpStatus.NOT_FOUND, "Product Not Found", null,
                        new ApiErrorResponse("not_found", "Product Not Found", "No product found with id " + id,
                                HttpStatus.NOT_FOUND)));
    }

    @PostMapping
    @Operation(summary = "Add a new product", description = "Create a new product")
    public ResponseEntity<ApiResponse<Product>> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED, "Product Created", savedProduct, null));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a product", description = "Update product by Id")
    public ResponseEntity<ApiResponse<Product>> updateProduct(@PathVariable Long id,
            @RequestBody Product newProductData) {
        Optional<Product> product = productService.findById(id);
        if (product.isPresent()) {
            Product updatedProduct = productService.updateProduct(id, newProductData);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK, "Updated", updatedProduct, null));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(HttpStatus.NOT_FOUND, "Product Not Found", null,
                        new ApiErrorResponse("not_found", "Product Not Found", "No product found with id " + id,
                                HttpStatus.NOT_FOUND)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product", description = "Delete product by Id")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        Optional<Product> product = productService.findById(id);
        if (product.isPresent()) {
            try {
                productService.softDelete(id);
                return ResponseEntity.noContent().build();
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error", null,
                                new ApiErrorResponse("server_error", "Server Error", "id " + id,
                                        HttpStatus.INTERNAL_SERVER_ERROR)));
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(HttpStatus.NOT_FOUND, "Product Not Found", null,
                        new ApiErrorResponse("not_found", "Product Not Found", "No product found with id " + id,
                                HttpStatus.NOT_FOUND)));
    }
}