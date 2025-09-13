package com.nimbleways.springboilerplate.services.product;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ProductExpiredProcessor implements ProductProcessor {

    private final ProductRepository productRepository;
    private final ProductService productService;

    @Override
    public void process(Product product) {
        if (isAvailableAndNotExpired(product)) {
            decrementAvailability(product);
        } else {
            productService.handleExpiredProduct(product);
        }
    }

    @Override
    public boolean supports(String productType) {
        return "EXPIRABLE".equals(productType);
    }

    private boolean isAvailableAndNotExpired(Product product) {
        return product.getAvailable() > 0
                && product.getExpiryDate().isAfter(LocalDate.now());
    }

    private void decrementAvailability(Product product) {
        product.setAvailable(product.getAvailable() - 1);
        productRepository.save(product);
    }
}
