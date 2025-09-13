package com.nimbleways.springboilerplate.services.product;


import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ProductSeasonalProcessor implements ProductProcessor {

    private final ProductRepository productRepository;
    private final ProductService productService;

    @Override
    public void process(Product product) {
        if (isInSeasonAndAvailable(product)) {
            decrementAvailability(product);
        } else {
            productService.handleSeasonalProduct(product);
        }
    }

    @Override
    public boolean supports(String productType) {
        return "SEASONAL".equals(productType);
    }

    private boolean isInSeasonAndAvailable(Product product) {
        LocalDate now = LocalDate.now();
        return now.isAfter(product.getSeasonStartDate())
                && now.isBefore(product.getSeasonEndDate())
                && product.getAvailable() > 0;
    }

    private void decrementAvailability(Product product) {
        product.setAvailable(product.getAvailable() - 1);
        productRepository.save(product);
    }
}