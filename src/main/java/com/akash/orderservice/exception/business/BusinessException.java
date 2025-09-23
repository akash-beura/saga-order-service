package com.akash.orderservice.exception.business;

import com.akash.orderservice.exception.OrderServiceBaseException;

public class BusinessException extends OrderServiceBaseException {

    BusinessException(String errorCode, String message) {
        super(errorCode, message);
    }

}
