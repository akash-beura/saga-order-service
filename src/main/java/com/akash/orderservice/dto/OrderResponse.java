package com.akash.orderservice.dto;

import com.akash.orderservice.model.Item;
import com.akash.orderservice.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private UUID id;
    private String userId;
    private Double amount;
    private List<Item> items;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
