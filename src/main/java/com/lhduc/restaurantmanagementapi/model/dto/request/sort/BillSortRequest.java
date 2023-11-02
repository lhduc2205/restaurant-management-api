package com.lhduc.restaurantmanagementapi.model.dto.request.sort;

import com.lhduc.restaurantmanagementapi.common.anotation.SortingConstraint;
import com.lhduc.restaurantmanagementapi.model.entity.Bill;

public class BillSortRequest extends SortRequest {
    @SortingConstraint(sortClass = Bill.class)
    private String sort;

    public void setSort(String sort) {
        super.parseSortString(sort);
        this.sort = sort;
    }
}
