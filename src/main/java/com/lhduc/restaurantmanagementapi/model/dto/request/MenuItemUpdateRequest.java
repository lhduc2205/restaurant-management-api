package com.lhduc.restaurantmanagementapi.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuItemUpdateRequest {
    private String name;
    private double price;
}
