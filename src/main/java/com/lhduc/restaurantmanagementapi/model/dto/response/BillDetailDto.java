package com.lhduc.restaurantmanagementapi.model.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BillDetailDto {
    private int quantity;

    private double pricePerUnit;

    private double totalPrice;

    private MenuItemDto menuItem;

    public BillDetailDto(int quantity, double pricePerUnit) {
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
    }

    public double getTotalPrice() {
        return pricePerUnit * quantity;
    }
}
