package com.lhduc.restaurantmanagementapi.model.dto.request.menuitem;

import jakarta.validation.constraints.Min;
import lombok.Data;

import static com.lhduc.restaurantmanagementapi.common.constant.MessageConstant.PRICE_MUST_BE_AT_LEAST_1;

@Data
public class MenuItemUpdateRequest {
    private String name;

    @Min(value = 1, message = PRICE_MUST_BE_AT_LEAST_1)
    private double price;
}
