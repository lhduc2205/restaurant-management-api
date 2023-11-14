package com.lhduc.restaurantmanagementapi.model.dto.request.bill;

import com.lhduc.restaurantmanagementapi.common.enums.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillFilter {
    private PaymentStatus paymentStatus;
}
