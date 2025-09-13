package com.nimbleways.springboilerplate.services.product;

import com.nimbleways.springboilerplate.exceptions.UnsupportedProductTypeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductProcessorFactory {

    private final List<ProductProcessor> processors;

    public ProductProcessor getProcessor(String productType) {
        return processors.stream()
                .filter(processor -> processor.supports(productType))
                .findFirst()
                .orElseThrow(() -> new UnsupportedProductTypeException(
                        "No processor found for product type: " + productType));
    }
}
