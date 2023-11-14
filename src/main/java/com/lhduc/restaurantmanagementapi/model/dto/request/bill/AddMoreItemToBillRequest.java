package com.lhduc.restaurantmanagementapi.model.dto.request.bill;

import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class AddMoreItemToBillRequest {
    @Valid
    private List<BillDetailCreateRequest> items;
}
