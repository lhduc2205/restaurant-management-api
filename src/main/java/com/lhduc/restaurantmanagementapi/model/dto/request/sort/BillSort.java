package com.lhduc.restaurantmanagementapi.model.dto.request.sort;

import com.lhduc.restaurantmanagementapi.common.anotation.SortingConstraint;
import com.lhduc.restaurantmanagementapi.model.entity.Bill;

public class BillSort extends SortRequest {
    @SortingConstraint(sortClass = Bill.class)
    private String sort;

    public void setSort(String sort) {
        super.splitSortToFields(sort);
        this.sort = sort;
    }
}
