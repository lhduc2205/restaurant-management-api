package com.lhduc.restaurantmanagementapi.model.dto.request.bill;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import static com.lhduc.restaurantmanagementapi.common.constant.MessageConstant.QUANTITY_MUST_BE_AT_LEAST_1;

@Getter
@Setter
public class BillDetailCreateRequest {
    private int menuItemId;

    @Min(value = 1, message = QUANTITY_MUST_BE_AT_LEAST_1)
    private Integer quantity;

    private String description;
}
