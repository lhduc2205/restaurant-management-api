package com.lhduc.restaurantmanagementapi.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationRequest {
    private int offset = 0;
    private int limit = 10;
}
