package com.lhduc.restaurantmanagementapi.exception.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.lhduc.restaurantmanagementapi.exception.NotFoundException;
import com.lhduc.restaurantmanagementapi.model.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@ControllerAdvice
public class DataRequestExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFormatException(InvalidFormatException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        if (this.isEnumOf(exception.getTargetType())) {
            this.handleInvalidEnum(errorResponse, exception);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    private boolean isEnumOf(Class<?> targetType) {
        return targetType != null && targetType.isEnum();
    }

    private void handleInvalidEnum(ErrorResponse errorResponse, InvalidFormatException exception) {
        String errorFieldName = exception.getPath().get(exception.getPath().size() - 1).getFieldName();
        String groupValue = Arrays.toString(exception.getTargetType().getEnumConstants());
        errorResponse.setMessage("Invalid enum value: " + exception.getValue());
        errorResponse.addError(errorFieldName, "The value must be one of: " + groupValue);
    }
}
