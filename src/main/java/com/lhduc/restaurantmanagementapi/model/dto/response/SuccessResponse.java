package com.lhduc.restaurantmanagementapi.model.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessResponse<T> {
    private String message;
    private T data;

    public static <T> SuccessResponse<T> of (T data) {
        SuccessResponse<T> successResponse = new SuccessResponse<>();
        successResponse.data = data;
        return successResponse;
    }

    public static <T> SuccessResponse<T> of (T data, String message) {
        SuccessResponse<T> successResponse = of(data);
        successResponse.message = message;
        return successResponse;
    }
}
