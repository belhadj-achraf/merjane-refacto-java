package com.nimbleways.springboilerplate.controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbleways.springboilerplate.contollers.OrderController;
import com.nimbleways.springboilerplate.dto.product.ProcessOrderResponse;
import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.services.OrderProcessingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@DisplayName("Order Controller Tests")
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderProcessingService orderProcessingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should successfully process order and return OK status")
    void processOrderShouldReturn() throws Exception {
        // Given
        Long orderId = 1L;
        ProcessOrderResponse expectedResponse = new ProcessOrderResponse(orderId);
        when(orderProcessingService.processOrder(orderId)).thenReturn(expectedResponse);

        // When & Then
        MvcResult result = mockMvc.perform(post("/api/v1/orders/{orderId}/process", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(orderId))
                .andDo(print())
                .andReturn();

        // Verify service was called
        verify(orderProcessingService).processOrder(orderId);

        // Verify response content
        String responseContent = result.getResponse().getContentAsString();
        ProcessOrderResponse actualResponse = objectMapper.readValue(responseContent, ProcessOrderResponse.class);
        assertEquals(expectedResponse.id(), actualResponse.id());
    }

    @Test
    @DisplayName("Should return 400 for invalid order ID format")
    void shouldReturn400ForInvalidOrderIdFormat() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/v1/orders/{orderId}/process", "invalid-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Verify service was not called
        verifyNoInteractions(orderProcessingService);
    }

    private static Order createOrder(Set<Product> products) {
        Order order = new Order();
        order.setItems(products);
        return order;
    }

    private static List<Product> createProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(null, 15, 30, "NORMAL", "USB Cable", null, null, null));
        products.add(new Product(null, 10, 0, "NORMAL", "USB Dongle", null, null, null));
        products.add(new Product(null, 15, 30, "EXPIRABLE", "Butter", LocalDate.now().plusDays(26), null,
                null));
        products.add(new Product(null, 90, 6, "EXPIRABLE", "Milk", LocalDate.now().minusDays(2), null, null));
        products.add(new Product(null, 15, 30, "SEASONAL", "Watermelon", null, LocalDate.now().minusDays(2),
                LocalDate.now().plusDays(58)));
        products.add(new Product(null, 15, 30, "SEASONAL", "Grapes", null, LocalDate.now().plusDays(180),
                LocalDate.now().plusDays(240)));
        return products;
    }
}