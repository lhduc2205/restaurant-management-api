package com.lhduc.restaurantmanagementapi.model.dto.request.bill;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillDetailCreateRequest {
    private int menuItemId;

    private int quantity;

    private String description;
}
