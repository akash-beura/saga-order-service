package com.akash.orderservice.event.producer;

import com.akash.events.dto.OrderCreatedEvent;
import com.akash.events.dto.enums.OrderStatus;
import com.akash.events.dto.enums.PaymentMode;
import com.akash.orderservice.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.UUID;

@Component
@Slf4j
public class OrderProducer {

    private static final String TOPIC = "order_created_event";
    private static final String CORRELATION_ID_HEADER = "x-correlation-id";

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public OrderProducer(KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderEvent(Order order) {
        try {
            OrderCreatedEvent event = prepareOrderEvent(order);
            ProducerRecord<String, OrderCreatedEvent> record = new ProducerRecord<>(TOPIC, event);

            String correlationId = MDC.get(CORRELATION_ID_HEADER);
            if (correlationId == null) {
                correlationId = UUID.randomUUID().toString();
            }
            record.headers().add(CORRELATION_ID_HEADER, correlationId.getBytes(StandardCharsets.UTF_8));
            kafkaTemplate.send(TOPIC, event);
            log.info("Published order to topic: order_created {}", event);
        } finally {
            MDC.clear();
        }
    }

    private OrderCreatedEvent prepareOrderEvent(Order order) {
        OrderCreatedEvent event = new OrderCreatedEvent();
        event.setUserId(order.getUserId());
        event.setOrderId(order.getId().toString());
        event.setStatus(OrderStatus.PROCESSING);
        event.setAmount(order.getAmount());
        event.setPaymentMode(new Random().nextInt(2) == 1 ? PaymentMode.PAYPAL : PaymentMode.UPI);
        event.setCreatedAt(order.getCreatedAt());
        return event;
    }


}
