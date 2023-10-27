package com.lhduc.restaurantmanagementapi.exception.handler;

import com.lhduc.restaurantmanagementapi.model.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Throwable throwable) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(throwable.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
