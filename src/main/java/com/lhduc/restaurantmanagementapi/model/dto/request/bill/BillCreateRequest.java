package com.lhduc.restaurantmanagementapi.model.dto.request.bill;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BillCreateRequest {

    @Valid
    private List<BillDetailCreateRequest> items = new ArrayList<>();
}
