package com.akash.orderservice.exception;

public class OrderServiceBaseException extends RuntimeException {

    private final String errorCode;

    public OrderServiceBaseException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }


}
