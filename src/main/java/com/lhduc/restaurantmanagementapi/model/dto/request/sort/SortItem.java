package com.lhduc.restaurantmanagementapi.model.dto.request.sort;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort.Direction;

@Getter
@Setter
public class SortItem {
    private String sortField;
    private Direction sortType;
}
