package com.akash.orderservice.model;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
@Builder
public class Order {

    @Id
    private UUID id;
    private String userId;
    private Double amount;
    @OneToMany
    @JoinTable(
            name = "orders_items",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> items;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @Type(JsonType.class)
//    @Column(columnDefinition = "jsonb")
    @Column(columnDefinition = "TEXT") // for H2 dev
    private ReprocessAttempt retryStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}


//id: UUID
//userId: String
//amount: BigDecimal
//items: List<Item>
//status: PENDING | CONFIRMED | CANCELLED
//        createdAt, updatedAt
//sagaState: { currentStep, attempts }