package com.lhduc.restaurantmanagementapi.model.dto.request.bill;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.lhduc.restaurantmanagementapi.common.constant.MessageConstant.MENU_ITEM_ID_CAN_NOT_BE_NULL;
import static com.lhduc.restaurantmanagementapi.common.constant.MessageConstant.PRICE_MUST_BE_AT_LEAST_1;
import static com.lhduc.restaurantmanagementapi.common.constant.MessageConstant.QUANTITY_MUST_BE_AT_LEAST_1;

@Getter
@Setter
@ToString
public class BillDetailUpdateRequest {
    @NotNull(message = MENU_ITEM_ID_CAN_NOT_BE_NULL)
    private Integer menuItemId;

    @Min(value = 1, message = QUANTITY_MUST_BE_AT_LEAST_1)
    private int quantity;

    @Min(value = 1, message = PRICE_MUST_BE_AT_LEAST_1)
    private double pricePerUnit;
}
