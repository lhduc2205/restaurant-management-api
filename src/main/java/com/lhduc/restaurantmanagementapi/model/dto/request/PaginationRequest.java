package com.lhduc.restaurantmanagementapi.model.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationRequest {
    public static final int DEFAULT_OFFSET = 0;
    public static final int DEFAULT_LIMIT = 10;

    @Min(0)
    private int offset = DEFAULT_OFFSET;

    @Min(1)
    private int limit = DEFAULT_LIMIT;
}
