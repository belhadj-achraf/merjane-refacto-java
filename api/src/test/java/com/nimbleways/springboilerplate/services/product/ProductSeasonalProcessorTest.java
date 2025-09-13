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
public class ProductSeasonalProcessorTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductSeasonalProcessor productSeasonalProcessor;

    private Product product;

    @BeforeEach
    void setUp() {
        product = createTestProduct();
        product.setType("SEASONAL");
    }

    @Test
    @DisplayName("Should support SEASONAL product type")
    void shouldSupportSeasonalProductType() {
        // When & Then
        assertTrue(productSeasonalProcessor.supports("SEASONAL"));
        assertFalse(productSeasonalProcessor.supports("NORMAL"));
        assertFalse(productSeasonalProcessor.supports("EXPIRABLE"));
    }

    @Test
    @DisplayName("Should decrement availability when in season and available")
    void shouldDecrementAvailabilityWhenInSeasonAndAvailable() {
        // Given
        LocalDate now = LocalDate.now();
        product.setAvailable(3);
        product.setSeasonStartDate(now.minusDays(10));
        product.setSeasonEndDate(now.plusDays(10));

        // When
        productSeasonalProcessor.process(product);

        // Then
        assertEquals(2, product.getAvailable());
        verify(productRepository).save(product);
        verifyNoInteractions(productService);
    }

    @Test
    @DisplayName("Should handle seasonal product when out of season")
    void shouldHandleSeasonalProductWhenOutOfSeason() {
        // Given
        LocalDate now = LocalDate.now();
        product.setAvailable(3);
        product.setSeasonStartDate(now.plusDays(10)); // Future start date
        product.setSeasonEndDate(now.plusDays(20));   // Future end date

        // When
        productSeasonalProcessor.process(product);

        // Then
        assertEquals(3, product.getAvailable()); // Should not change
        verify(productService).handleSeasonalProduct(product);
        verifyNoInteractions(productRepository);
    }

    private Product createTestProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Seasonal Product");
        return product;
    }
}
