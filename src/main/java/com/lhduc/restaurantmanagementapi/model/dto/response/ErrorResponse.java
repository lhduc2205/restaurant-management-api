package com.lhduc.restaurantmanagementapi.model.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ErrorResponse {
    private String message;
    private Map<String, String> errors = new HashMap<>();
}
