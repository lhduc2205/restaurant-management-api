package com.lhduc.restaurantmanagementapi.model.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillDetailDto {
    private int quantity;

    private String description;

    private double pricePerUnit;

    private double totalPrice;

    private MenuItemDto menuItem;

    public double getTotalPrice() {
        return pricePerUnit * quantity;
    }
}
