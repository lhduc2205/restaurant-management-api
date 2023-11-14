package com.lhduc.restaurantmanagementapi.model.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BillDetailDto {
    private int quantity;

    private String description;

    private double pricePerUnit;

    private double totalPrice;

    private MenuItemDto menuItem;

    public BillDetailDto(int quantity, String description, double pricePerUnit) {
        this.quantity = quantity;
        this.description = description;
        this.pricePerUnit = pricePerUnit;
    }

    public double getTotalPrice() {
        return pricePerUnit * quantity;
    }
}
