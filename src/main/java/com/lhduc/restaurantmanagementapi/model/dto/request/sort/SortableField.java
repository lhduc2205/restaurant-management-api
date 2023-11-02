package com.lhduc.restaurantmanagementapi.model.dto.request.sort;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort.Direction;

@Getter
@Setter
public class SortableField {
    private String fieldName;
    private Direction sortDirection;
}
