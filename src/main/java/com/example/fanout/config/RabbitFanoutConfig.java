package com.example.fanout.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitFanoutConfig {

    // 1. Name the exchange
    public static final String ORDER_EXCHANGE = "order.fanout.exchange";

    // 2. Name the separate queues for each independent service
    public static final String BILLING_QUEUE = "order.billing.queue";
    public static final String FRAUD_QUEUE = "order.fraud.queue";

    @Bean
    public FanoutExchange orderExchange() {
        return new FanoutExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Queue billingQueue() {
        return new Queue(BILLING_QUEUE, true); // true = durable across broker restarts
    }

    @Bean
    public Queue fraudQueue() {
        return new Queue(FRAUD_QUEUE, true);
    }

    // 3. Bind both queues to the same fanout exchange
    @Bean
    public Binding bindBilling(Queue billingQueue, FanoutExchange orderExchange) {
        return BindingBuilder.bind(billingQueue).to(orderExchange);
    }

    @Bean
    public Binding bindFraud(Queue fraudQueue, FanoutExchange orderExchange) {
        return BindingBuilder.bind(fraudQueue).to(orderExchange);
    }
}
