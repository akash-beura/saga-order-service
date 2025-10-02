package com.akash.orderservice.service;

import com.akash.dto.PaymentCompletionEvent;
import com.akash.dto.enums.PaymentStatus;
import com.akash.orderservice.dto.OrderRequest;
import com.akash.orderservice.dto.OrderResponse;
import com.akash.orderservice.event.producer.OrderProducer;
import com.akash.orderservice.exception.business.OrderNotFoundException;
import com.akash.orderservice.mapper.OrderMapper;
import com.akash.orderservice.model.Item;
import com.akash.orderservice.model.Order;
import com.akash.orderservice.model.OrderStatus;
import com.akash.orderservice.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderProducer orderProducer;
    private final ItemService itemService;


    @Transactional
    public String createOrder(OrderRequest orderRequest) {
        Order order = prepareOrder(orderRequest);
        orderRepository.save(order);
        orderProducer.sendOrderEvent(order);
        log.info("Order created and under processing: {}", order);
        return order.getId().toString();
    }

    public OrderResponse fetchOrder(String orderId) {
        return orderMapper.toResponse(
                orderRepository.findById(UUID.fromString(orderId))
                        .orElseThrow(() -> new OrderNotFoundException(orderId))
        );
    }

    public List<OrderResponse> fetchAllOrders() {
        return orderMapper.toResponseList(orderRepository.findAll());
    }

    public void cancelOrder(String orderId) {
        Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        //TODO: Mock Initiate Refund
        orderMapper.toResponse(order);
    }

    private Order prepareOrder(OrderRequest orderRequest) {
        Order order = orderMapper.toEntity(orderRequest);
        order.setItems(itemService.findAllItemsByIds(orderRequest.itemIds));
        order.setUserId(orderRequest.getUserId());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(order.getCreatedAt());
        order.setStatus(OrderStatus.PROCESSING);
        order.setId(UUID.randomUUID());
        order.setAmount(order.getItems()
                .stream()
                .mapToDouble(Item::getItemPrice)
                .sum());
        return order;
    }

    @Transactional
    public void handlePaymentEvent(PaymentCompletionEvent event) {
        log.info("Payment event received: {}", event);
        Order order = orderRepository.findById(UUID.fromString(event.getOrderId()))
                .orElseThrow(() -> new OrderNotFoundException(event.getOrderId()));
        if (event.getPaymentStatus().equals(PaymentStatus.SUCCESS)) {
            order.setStatus(OrderStatus.COMPLETED);
        } else {
            order.setStatus(OrderStatus.FAILED);
        }
        orderRepository.save(order);
    }

}
