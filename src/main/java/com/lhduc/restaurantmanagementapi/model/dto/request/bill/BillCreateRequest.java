package com.lhduc.restaurantmanagementapi.model.dto.request.bill;

import jakarta.validation.Valid;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BillCreateRequest {

    @Valid
    private List<BillDetailCreateRequest> items = new ArrayList<>();
}
