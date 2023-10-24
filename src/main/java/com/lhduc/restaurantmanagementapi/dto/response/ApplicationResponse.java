package com.lhduc.restaurantmanagementapi.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationResponse {
    private int statusCode;
    private String message;
    private Object data;
}
