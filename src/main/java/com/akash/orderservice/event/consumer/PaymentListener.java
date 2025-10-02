package com.akash.orderservice.event.consumer;

import com.akash.dto.PaymentCompletionEvent;
import com.akash.orderservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class PaymentListener {

    private final OrderService orderService;

    public PaymentListener(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(
            topics = "PAYMENT_SUCCESS_TOPIC",
            groupId = "payment-processing-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handlePaymentSuccessEvents(ConsumerRecord<String, PaymentCompletionEvent> record) {
        handleMDC(record.headers());
        log.info("Payment_Success event received: {}", record.value());
        orderService.handlePaymentEvent(record.value());
        log.info("Payment_Success event processed");
    }

    @KafkaListener(
            topics = "PAYMENT_FAILED_TOPIC",
            groupId = "payment-processing-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handlePaymentFailureEvents(ConsumerRecord<String, PaymentCompletionEvent> record) {
        handleMDC(record.headers());
        log.info("Payment_Failed event received: {}", record.value());
        orderService.handlePaymentEvent(record.value());
        log.info("Payment_Failed event processed");
    }

    void handleMDC(Headers headers) {
        String correlationId = null;
        if (headers.lastHeader("x-correlation-id") != null) {
            correlationId = new String(headers
                    .lastHeader("x-correlation-id")
                    .value(), StandardCharsets.UTF_8);
        }
        MDC.put("x-correlation-id", correlationId);
    }


}
