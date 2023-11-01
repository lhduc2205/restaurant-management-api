package com.lhduc.restaurantmanagementapi.model.dto.request.bill;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillDetailUpdateRequest {
    private int menuItemId;

    private int quantity;

    private double pricePerUnit;

    private String description;
}
