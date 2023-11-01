package com.lhduc.restaurantmanagementapi.model.dto.request.bill;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BillCreateRequest {
    private List<BillDetailCreateRequest> details;
}
