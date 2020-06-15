package com.dbs.cartservice.controller;

import com.dbs.cartservice.dto.ProductDetails;
import com.dbs.cartservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${cart.api-prefix}")
public class ProductController {

    @Autowired
    private ProductService service;

    @PostMapping
    public ResponseEntity addProduct(@RequestBody List<ProductDetails> details) {
        service.addProduct(details);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
