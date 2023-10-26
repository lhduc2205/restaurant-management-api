package com.lhduc.restaurantmanagementapi.model.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationResponse {
    private int statusCode;
    private String message;
    private Object data;
}
