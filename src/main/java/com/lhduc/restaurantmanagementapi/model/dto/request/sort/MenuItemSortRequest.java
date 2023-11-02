package com.lhduc.restaurantmanagementapi.model.dto.request.sort;

import com.lhduc.restaurantmanagementapi.common.anotation.SortingConstraint;
import com.lhduc.restaurantmanagementapi.model.entity.MenuItem;

public class MenuItemSortRequest extends SortRequest {
    @SortingConstraint(sortClass = MenuItem.class)
    private String sort;

    public void setSort(String sort) {
        super.parseSortString(sort);
        this.sort = sort;
    }
}
