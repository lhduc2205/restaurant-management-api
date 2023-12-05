package com.lhduc.restaurantmanagementapi.model.dto.request.menuitem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.lhduc.restaurantmanagementapi.common.constant.MessageConstant.NAME_MUST_NOT_BE_BLANK;
import static com.lhduc.restaurantmanagementapi.common.constant.MessageConstant.PRICE_MUST_BE_AT_LEAST_1;

@Getter
@Setter
@ToString
public class MenuItemCreateRequest {
    @NotBlank(message = NAME_MUST_NOT_BE_BLANK)
    private String name;

    @Min(value = 1, message = PRICE_MUST_BE_AT_LEAST_1)
    private double price;
}
