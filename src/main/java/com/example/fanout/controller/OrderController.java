package com.example.fanout.controller;

import com.example.fanout.publisher.OrderPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderPublisher orderPublisher;

    public OrderController(OrderPublisher orderPublisher) {
        this.orderPublisher = orderPublisher;
    }

    // Now accepts: {"orderId": "XYZ-123"}
    @PostMapping
    public ResponseEntity<Map<String, String>> createOrder(@RequestBody Map<String, String> payload) {
        String orderId = payload.get("orderId");
        
        if (orderId == null || orderId.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing orderId in JSON body"));
        }

        orderPublisher.publishOrderCreatedEvent(orderId);
        
        return ResponseEntity.ok(Map.of(
            "status", "Accepted",
            "message", "Order event published successfully."
        ));
    }
}
