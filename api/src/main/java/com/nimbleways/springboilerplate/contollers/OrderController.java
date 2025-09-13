package com.nimbleways.springboilerplate.contollers;

import com.nimbleways.springboilerplate.dto.product.ProcessOrderResponse;
import com.nimbleways.springboilerplate.services.OrderProcessingService;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderProcessingService orderProcessingService;

    @PostMapping("/{orderId}/process")
    public ResponseEntity<ProcessOrderResponse> processOrder(
            @PathVariable @NotNull Long orderId) {

        log.info("Processing order with ID: {}", orderId);

        ProcessOrderResponse response = orderProcessingService.processOrder(orderId);

        log.info("Successfully processed order with ID: {}", orderId);

        return ResponseEntity.ok(response);
    }
}
