package com.akash.orderservice.controller;

import com.akash.orderservice.dto.OrderRequest;
import com.akash.orderservice.dto.OrderResponse;
import com.akash.orderservice.mapper.OrderMapper;
import com.akash.orderservice.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    public OrderController(OrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest request) {
        String orderId = orderService.createOrder(request);
        return ResponseEntity.created(URI.create("/orders/" + orderId)).body(orderId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable String id) {
        return ResponseEntity.ok(orderService.fetchOrder(id));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> listOrders() {
        return ResponseEntity.ok(orderService.fetchAllOrders());
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable String id) {
        orderService.cancelOrder(id); // may throw BusinessException
        return ResponseEntity.noContent().build();
    }


}
