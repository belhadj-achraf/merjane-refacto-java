package com.nimbleways.springboilerplate.services.product;

import com.nimbleways.springboilerplate.entities.Product;

public interface ProductProcessor {
    void process(Product product);
    boolean supports(String productType);
}