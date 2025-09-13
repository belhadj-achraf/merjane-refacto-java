package com.nimbleways.springboilerplate.services.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductProcessorFactoryTest {

    @Mock
    private ProductNormalProcessor productNormalProcessor;

    @Mock
    private ProductSeasonalProcessor productSeasonalProcessor;

    @Mock
    private ProductExpiredProcessor productExpiredProcessor;

    @InjectMocks
    private ProductProcessorFactory productProcessorFactory;

    @BeforeEach
    void setUp() {
        // Setup mock behaviors
        when(productNormalProcessor.supports("NORMAL")).thenReturn(true);
        when(productNormalProcessor.supports(anyString())).thenReturn(false);

        when(productSeasonalProcessor.supports("SEASONAL")).thenReturn(true);
        when(productSeasonalProcessor.supports(anyString())).thenReturn(false);

        when(productExpiredProcessor.supports("EXPIRABLE")).thenReturn(true);
        when(productExpiredProcessor.supports(anyString())).thenReturn(false);

        // Reset the mock behaviors to be more specific
        reset(productNormalProcessor, productSeasonalProcessor, productExpiredProcessor);
        when(productNormalProcessor.supports("NORMAL")).thenReturn(true);
        when(productSeasonalProcessor.supports("SEASONAL")).thenReturn(true);
        when(productExpiredProcessor.supports("EXPIRABLE")).thenReturn(true);
    }

    @Test
    @DisplayName("Should return NormalProductProcessor for NORMAL type")
    void shouldReturnNormalProductProcessorForNormalType() {
        // When
        ProductProcessor processor = productProcessorFactory.getProcessor("NORMAL");

        // Then
        assertEquals(productNormalProcessor, processor);
        verify(productNormalProcessor).supports("NORMAL");
    }

}
