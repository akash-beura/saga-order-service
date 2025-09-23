package com.akash.orderservice.event.producer;

import com.akash.orderservice.model.Order;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderProducer {

    private final KafkaTemplate<String, Order> kafkaTemplate;

    public OrderProducer(KafkaTemplate<String, Order> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @CircuitBreaker(name = "kafkaPublisher", fallbackMethod = "fallbackSendOrderEvent")
    @Retry(name = "kafkaPublisherRetry")
    public void sendOrderEvent(Order order) {
        kafkaTemplate.send("order_created", order);
        log.info("Published order to topic: order_created");
    }

    public void fallbackSendOrderEvent(KafkaException e, Order order) {
        log.error("All retries failed for order {}." +
                " Persisting event for manual retry.", order.getId(), e);
    }

}
