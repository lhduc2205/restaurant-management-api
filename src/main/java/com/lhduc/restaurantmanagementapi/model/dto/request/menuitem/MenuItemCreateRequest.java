package com.lhduc.restaurantmanagementapi.model.dto.request.menuitem;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuItemCreateRequest {
    private String name;
    private double price;
}