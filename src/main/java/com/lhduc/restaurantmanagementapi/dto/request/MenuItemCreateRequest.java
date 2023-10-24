package com.lhduc.restaurantmanagementapi.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuItemCreateRequest {
    private String name;
    private double price;
}
