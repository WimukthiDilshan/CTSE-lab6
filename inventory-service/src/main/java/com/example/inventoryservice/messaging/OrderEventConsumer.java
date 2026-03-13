package com.example.inventoryservice.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderEventConsumer {

    @KafkaListener(topics = "order-topic", groupId = "inventory-group")
    public void consume(String message) {
        log.info("Inventory updated for order: {}", message);
    }
}