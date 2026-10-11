package com.example.fanout.consumer;

import com.example.fanout.config.RabbitFanoutConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FanoutConsumers {

    // Service 1: Billing Engine
    @RabbitListener(queues = RabbitFanoutConfig.BILLING_QUEUE)
    public void receiveForBilling(String message) {
        System.out.println(" [Billing Service] Consumed order event -> Processing credit card payment: " + message);
        // Payment processing logic goes here...
    }

    // Service 2: Fraud Detection Scanner
    @RabbitListener(queues = RabbitFanoutConfig.FRAUD_QUEUE)
    public void receiveForFraudCheck(String message) {
        System.out.println(" [Fraud Service] Consumed order event -> Running risk heuristics algorithms: " + message);
        // Analytics or fraud validation logic goes here...
    }
}
