package com.example.product.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

@ExtendWith(MockitoExtension.class)
class ErrorControllerTest {

    @Test
    void testHandleError() {
        ErrorController errorController = new ErrorController();

        ModelAndView modelAndView = errorController.handleError();

        assertNotNull(modelAndView);
        assertEquals("error/404", modelAndView.getViewName());
        assertEquals(HttpStatus.NOT_FOUND.value(), modelAndView.getStatus().value());
    }
}
