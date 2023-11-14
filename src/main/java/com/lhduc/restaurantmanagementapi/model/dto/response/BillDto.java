package com.lhduc.restaurantmanagementapi.model.dto.response;

import com.lhduc.restaurantmanagementapi.common.enums.PaymentStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BillDto {
    private int id;

    private PaymentStatus paymentStatus = PaymentStatus.UNPAID;

    private double totalPrice;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<BillDetailDto> details = new ArrayList<>();

    public BillDto(int id, List<BillDetailDto> details) {
        this.id = id;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.details = new ArrayList<>(details);
    }

    public double getTotalPrice() {
        return details.stream().reduce(0d, (total, detail) -> total + detail.getTotalPrice(), Double::sum);
    }
}
