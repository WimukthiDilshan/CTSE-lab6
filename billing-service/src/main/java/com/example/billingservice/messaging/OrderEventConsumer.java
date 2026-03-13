package com.example.billingservice.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderEventConsumer {

    @KafkaListener(topics = "order-topic", groupId = "billing-group")
    public void consume(String message) {
        log.info("Invoice generated for order: {}", message);
    }
}