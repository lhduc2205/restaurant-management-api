package com.lhduc.restaurantmanagementapi.model.dto.request.menuitem;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuItemFilter {
    private String name;

    @Min(0)
    private Double price;
}
