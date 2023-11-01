package com.lhduc.restaurantmanagementapi.model.dto.request.bill;

import com.lhduc.restaurantmanagementapi.common.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.lhduc.restaurantmanagementapi.common.constant.MessageConstant.PAYMENT_STATUS_CAN_NOT_BE_EMPTY;

@Getter
@Setter
public class BillUpdateRequest {
    @NotNull(message = PAYMENT_STATUS_CAN_NOT_BE_EMPTY)
    private PaymentStatus paymentStatus;

    private List<BillDetailUpdateRequest> details;
}
