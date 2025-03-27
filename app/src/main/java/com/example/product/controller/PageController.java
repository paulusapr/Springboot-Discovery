package com.example.product.controller;

import com.example.product.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
  private final ProductService productService;

  public PageController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping("/")
  public String showProducts(Model model) {
    model.addAttribute("products", productService.getAllProducts());
    return "product"; // Refers to product.html in templates/
  }

  @GetMapping("/error-test")
  public void errorTest() {
    throw new RuntimeException("This is a test exception!");
  }

}
