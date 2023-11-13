package com.lhduc.restaurantmanagementapi.model.mappers;

import com.lhduc.restaurantmanagementapi.model.dto.request.menuitem.MenuItemCreateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.menuitem.MenuItemFilter;
import com.lhduc.restaurantmanagementapi.model.dto.response.MenuItemDto;
import com.lhduc.restaurantmanagementapi.model.entity.MenuItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuItemMapper extends BaseMapper<MenuItemDto, MenuItem> {
    /**
     * Converts a MenuItemCreateRequest DTO to MenuItem entity.
     *
     * @param requestDto The MenuItemCreateRequest object to convert.
     * @return A MenuItem entity representing the data from the DTO.
     */
    MenuItem convertToEntityFromRequest(MenuItemCreateRequest requestDto);

    /**
     * Converts a MenuItemFilter DTO to MenuItem entity.
     *
     * @param menuItemFilter The MenuItemFilter object to convert.
     * @return A MenuItem entity representing the data from the DTO.
     */
    MenuItem convertToEntityFromFilter(MenuItemFilter menuItemFilter);
}
