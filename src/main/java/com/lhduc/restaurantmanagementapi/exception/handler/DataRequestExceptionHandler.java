package com.lhduc.restaurantmanagementapi.exception.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.lhduc.restaurantmanagementapi.exception.NotFoundException;
import com.lhduc.restaurantmanagementapi.exception.OperationForbiddenException;
import com.lhduc.restaurantmanagementapi.model.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@Slf4j
@ControllerAdvice
public class DataRequestExceptionHandler {

    /**
     * Exception handler method to handle NotFoundException instances.
     *
     * @param exception The NotFoundException instance to handle.
     * @return A ResponseEntity with an ErrorResponse containing the error message and HTTP status code.
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException exception) {
        log.error(exception.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorMessage(exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Exception handler method to handle InvalidFormatException instances.
     *
     * @param exception The InvalidFormatException instance to handle.
     * @return A ResponseEntity with an ErrorResponse containing the error message and HTTP status code.
     */
    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFormatException(InvalidFormatException exception) {
        log.error(exception.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        if (this.isEnumOf(exception.getTargetType())) {
            this.handleInvalidEnum(errorResponse, exception);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Exception handler method to handle OperationForbiddenException instances.
     *
     * @param exception The OperationForbiddenException instance to handle.
     * @return A ResponseEntity with an ErrorResponse containing the error message and HTTP status code.
     */
    @ExceptionHandler(OperationForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleOperationForbiddenException(OperationForbiddenException exception) {
        log.error(exception.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorMessage(exception.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    /**
     * Checks if the given class represents an enum type.
     *
     * @param targetType The class is to be checked for being an enum type.
     * @return {@code true} if the targetType is an enum, {@code false} otherwise.
     */
    private boolean isEnumOf(Class<?> targetType) {
        return targetType != null && targetType.isEnum();
    }

    /**
     * Handles an InvalidFormatException related to invalid enum values and populates an ErrorResponse.
     *
     * @param errorResponse The ErrorResponse is to be populated with error information.
     * @param exception     The InvalidFormatException to be handled.
     */
    private void handleInvalidEnum(ErrorResponse errorResponse, InvalidFormatException exception) {
        String groupValue = Arrays.toString(exception.getTargetType().getEnumConstants());
        errorResponse.setErrorMessage("Invalid enum value: " + exception.getValue() + ". The value must be one of: " + groupValue);
    }
}
