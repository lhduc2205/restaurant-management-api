package com.lhduc.restaurantmanagementapi.model.dto.request.bill;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class UpdateBillItemRequest {
    @Valid
    private List<BillDetailUpdateRequest> items = new ArrayList<>();
}
