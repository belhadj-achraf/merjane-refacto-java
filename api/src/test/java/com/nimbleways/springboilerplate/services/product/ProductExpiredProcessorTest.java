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

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductExpiredProcessorTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductExpiredProcessor productExpiredProcessor;

    private Product product;

    @BeforeEach
    void setUp() {
        product = createTestProduct();
        product.setType("EXPIRABLE");
    }

    @Test
    @DisplayName("Should support EXPIRABLE product type")
    void shouldSupportExpirableProductType() {
        // When & Then
        assertTrue(productExpiredProcessor.supports("EXPIRABLE"));
        assertFalse(productExpiredProcessor.supports("NORMAL"));
        assertFalse(productExpiredProcessor.supports("SEASONAL"));
    }

    @Test
    @DisplayName("Should decrement availability when available and not expired")
    void shouldDecrementAvailabilityWhenAvailableAndNotExpired() {
        // Given
        product.setAvailable(4);
        product.setExpiryDate(LocalDate.now().plusDays(5)); // Future expiry date

        // When
        productExpiredProcessor.process(product);

        // Then
        assertEquals(3, product.getAvailable());
        verify(productRepository).save(product);
        verifyNoInteractions(productService);
    }

    private Product createTestProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Expirable Product");
        return product;
    }
}
