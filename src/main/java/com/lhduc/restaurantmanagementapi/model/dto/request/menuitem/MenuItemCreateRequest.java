package com.lhduc.restaurantmanagementapi.model.dto.request.menuitem;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.lhduc.restaurantmanagementapi.common.constant.MessageConstant.PRICE_MUST_BE_AT_LEAST_1;

@Getter
@Setter
@ToString
public class MenuItemCreateRequest {
    private String name;

    @Min(value = 1, message = PRICE_MUST_BE_AT_LEAST_1)
    private double price;
}
