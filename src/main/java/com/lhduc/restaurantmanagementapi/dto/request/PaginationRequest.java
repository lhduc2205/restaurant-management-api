package com.lhduc.restaurantmanagementapi.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationRequest {
    private int pageNumber = 1;
    private int pageSize = 10;
}
