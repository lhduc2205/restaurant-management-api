package com.lhduc.restaurantmanagementapi.exception;

import com.lhduc.restaurantmanagementapi.exception.ApplicationException;

public class NotFoundException extends ApplicationException {

    public NotFoundException(String message) {
        super(message);
    }
}
