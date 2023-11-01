package com.lhduc.restaurantmanagementapi.exception;

public class OperationForbiddenException extends ApplicationException {
    public OperationForbiddenException() {
        super();
    }

    public OperationForbiddenException(String message) {
        super(message);
    }

    public OperationForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}
