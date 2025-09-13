package com.nimbleways.springboilerplate.services.product;


import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductNormalProcessor implements ProductProcessor {

    private final ProductRepository productRepository;
    private final ProductService productService;

    @Override
    public void process(Product product) {
        if (product.getAvailable() > 0) {
            decrementAvailability(product);
        } else {
            handleOutOfStock(product);
        }
    }

    @Override
    public boolean supports(String productType) {
        return "NORMAL".equals(productType);
    }

    private void decrementAvailability(Product product) {
        product.setAvailable(product.getAvailable() - 1);
        productRepository.save(product);
    }

    private void handleOutOfStock(Product product) {
        int leadTime = product.getLeadTime();
        if (leadTime > 0) {
            productService.notifyDelay(leadTime, product);
        }
    }
}
