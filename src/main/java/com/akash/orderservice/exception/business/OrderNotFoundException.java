package com.akash.orderservice.exception.business;

import com.akash.orderservice.exception.OrderServiceBaseException;

public class OrderNotFoundException extends OrderServiceBaseException {

    public OrderNotFoundException(String orderId) {
        super("404", "Order with ID " + orderId + " not found");
    }

}
