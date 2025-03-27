package com.example.product.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/error")
public class ErrorController {

  @GetMapping
  public ModelAndView handleError() {
    ModelAndView modelAndView = new ModelAndView("error/404");
    modelAndView.setStatus(HttpStatus.NOT_FOUND);
    return modelAndView;
  }
}
