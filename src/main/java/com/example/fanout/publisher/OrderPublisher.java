package com.example.fanout.publisher;

import com.example.fanout.config.RabbitFanoutConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderPublisher {

    private final RabbitTemplate rabbitTemplate;

    public OrderPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishOrderCreatedEvent(String orderId) {
        String payload = "{\"orderId\":\"" + orderId + "\", \"status\":\"CREATED\"}";
        
        System.out.println(" [Order Service] Emitting event for order: " + orderId);
        
        // Arguments: (Exchange Name, Routing Key, Message Payload)
        rabbitTemplate.convertAndSend(RabbitFanoutConfig.ORDER_EXCHANGE, "", payload);
    }
}
