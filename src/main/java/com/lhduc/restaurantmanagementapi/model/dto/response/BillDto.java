package com.lhduc.restaurantmanagementapi.model.dto.response;

import com.lhduc.restaurantmanagementapi.common.enums.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BillDto {
    private int id;

    private PaymentStatus paymentStatus;

    private double totalPrice;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<BillDetailDto> details;

    public double getTotalPrice() {
        return details.stream().reduce(0d, (total, detail) -> total + detail.getTotalPrice(), Double::sum);
    }
}
