package com.example.product.component;

import com.example.product.model.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ProductComponent {

  private final List<Product> productList = new ArrayList<>();

  public ProductComponent() {
    productList.add(new Product(1, "Laptop", 1500.00));
    productList.add(new Product(2, "Smartphone", 800.00));
    productList.add(new Product(3, "Tablet", 500.00));
  }

  public List<Product> getProducts() {
    return productList;
  }

  public void addProduct(Product product) {
    productList.add(product);
  }

  public Optional<Product> findById(long id) {
    return productList.stream().filter(p -> p.getId() == id).findFirst();
  }

  public Product save(Product product) {
    productList.add(product);
    return product;
  }
}