package com.example.product.controller;

import com.example.product.dto.ApiResponse;
import com.example.product.dto.ApiErrorResponse;
import com.example.product.model.Product;
import com.example.product.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

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
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", products, null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieve a product by its ID")
    public ResponseEntity<?> getProductById(@PathVariable long id) {
        Optional<Product> product = productService.findById(id);
        if (product.isPresent()) {
            return ResponseEntity.ok(new ApiResponse<>(200, "Success", product.get(), null));
        }
        return ResponseEntity.status(404)
                .body(new ApiResponse<>(404, "Product Not Found", null,
                        new ApiErrorResponse("not_found", "Product Not Found", "No product found with id " + id, 404)));
    }

    @PostMapping
    @Operation(summary = "Add a new product", description = "Create a new product")
    public ResponseEntity<ApiResponse<Product>> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.save(product);
        return ResponseEntity.ok(new ApiResponse<>(201, "Product Created", savedProduct, null));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a product", description = "Update product by Id")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product newProductData) {
        Optional<Product> product = productService.findById(id);
        if (product.isPresent()) {
            Product updateProduct = productService.updateProduct(id, newProductData);
            return ResponseEntity.ok(new ApiResponse<>(200, "Updated", updateProduct, null));
        }
        return ResponseEntity.status(404)
                .body(new ApiResponse<>(404, "Product Not Found", null,
                        new ApiErrorResponse("not_found", "Product Not Found", "No product found with id " + id, 404)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product", description = "Delete product by Id")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        Optional<Product> product = productService.findById(id);
        if (product.isPresent()) {
            try {
                productService.softDelete(id);
                return ResponseEntity.ok(new ApiResponse<>(200, "Success", "Product deleted successfully", null));
            } catch (Exception e) {
                return ResponseEntity.status(500)
                        .body(new ApiResponse<>(500, "Server Error", null,
                                new ApiErrorResponse("server_error", "Server Error", "id " + id, 404)));
            }
        }
        return ResponseEntity.status(404)
                .body(new ApiResponse<>(404, "Product Not Found", null,
                        new ApiErrorResponse("not_found", "Product Not Found", "No product found with id " + id, 404)));
    }
}