package com.lhduc.restaurantmanagementapi.model.mappers;

import com.lhduc.restaurantmanagementapi.model.dto.request.menuitem.MenuItemCreateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.response.MenuItemDto;
import com.lhduc.restaurantmanagementapi.model.entity.MenuItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuItemMapper extends BaseMapper<MenuItemDto, MenuItem> {
    MenuItem convertToEntityFromRequest(MenuItemCreateRequest requestDto);
}
