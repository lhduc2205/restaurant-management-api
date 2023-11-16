package com.lhduc.restaurantmanagementapi.model.dto.request.bill;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillDetailUpdateRequest {
    private int menuItemId;

    @Min(1)
    private int quantity;

    private double pricePerUnit;
}
