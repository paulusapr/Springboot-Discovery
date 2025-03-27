package com.example.product.service;

import com.example.product.model.Product;
import org.springframework.stereotype.Service;
import com.example.product.repository.ProductRepository;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
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

    public Optional<Product> findById(long id) {
        return productRepository.findById(id);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product newProductData) {
        return productRepository.findById(id).map(product -> {
            product.setName(newProductData.getName());
            product.setPrice(newProductData.getPrice());
            return productRepository.save(product);
        }).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Transactional
    public void softDelete(Long id) {
        productRepository.findById(id).ifPresent(product -> {
            product.setDeletedAt(LocalDateTime.now());
            productRepository.save(product);
        });
    }

    public void hardDelete(Long id) {
        productRepository.deleteById(id);
    }

    // ================ Component ===================

    // private final ProductComponent productComponent;

    // public ProductService(ProductComponent productComponent) {
    // this.productComponent = productComponent;
    // }

    // public List<Product> getAllProducts() {
    // return productComponent.getProducts();
    // }

    // public Optional<Product> findById(long id) {
    // return productComponent.findById(id);
    // }

    // public Product save(Product product) {
    // return productComponent.save(product);
    // }

    // public void addProduct(Product product) {
    // productComponent.addProduct(product);
    // }
}