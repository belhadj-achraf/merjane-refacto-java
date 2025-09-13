package com.nimbleways.springboilerplate.services.product;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductNormalProcessorTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductNormalProcessor productNormalProcessor;

    private Product product;

    @BeforeEach
    void setUp() {
        product = createTestProduct();
        product.setType("NORMAL");
    }

    @Test
    @DisplayName("Should support NORMAL product type")
    void shouldSupportNormalProductType() {
        // When & Then
        assertTrue(productNormalProcessor.supports("NORMAL"));
        assertFalse(productNormalProcessor.supports("SEASONAL"));
        assertFalse(productNormalProcessor.supports("EXPIRABLE"));
    }

    @Test
    @DisplayName("Should decrement availability when product is available")
    void shouldDecrementAvailabilityWhenProductIsAvailable() {
        product.setAvailable(5);

        productNormalProcessor.process(product);

        assertEquals(4, product.getAvailable());
        verify(productRepository).save(product);
        verifyNoInteractions(productService);
    }


    private Product createTestProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        return product;
    }
}

