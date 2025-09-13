package com.nimbleways.springboilerplate.services;

import com.nimbleways.springboilerplate.dto.product.ProcessOrderResponse;
import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.exceptions.OrderNotFoundException;
import com.nimbleways.springboilerplate.repositories.OrderRepository;
import com.nimbleways.springboilerplate.services.product.ProductProcessorFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderProcessingService {

    private final OrderRepository orderRepository;
    private final ProductProcessorFactory productProcessorFactory;

    @Transactional
    public ProcessOrderResponse processOrder(Long orderId) {
        Order order = findOrderById(orderId);

        log.debug("Processing {} products for order {}", order.getItems().size(), orderId);

        for (Product product : order.getItems()) {
            processProduct(product);
        }

        return new ProcessOrderResponse(order.getId());
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));
    }

    private void processProduct(Product product) {
        var processor = productProcessorFactory.getProcessor(product.getType());
        processor.process(product);
    }
}