package com.lhduc.restaurantmanagementapi.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuItemDto {
    private int id;
    private String name;
    private double price;
}
