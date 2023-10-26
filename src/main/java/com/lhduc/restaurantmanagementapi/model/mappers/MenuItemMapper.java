package com.lhduc.restaurantmanagementapi.model.mappers;

import com.lhduc.restaurantmanagementapi.model.dto.response.MenuItemDto;
import com.lhduc.restaurantmanagementapi.model.entity.MenuItem;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface MenuItemMapper extends BaseMapper<MenuItemDto, MenuItem> {
    @Override
    MenuItemDto convertToDto(MenuItem menuItem);

    @Override
    List<MenuItemDto> convertToDto(Collection<MenuItem> entity);

    @Override
    MenuItem convertToEntity(MenuItemDto menuItemDto);

    @Override
    List<MenuItem> convertToEntity(Collection<MenuItemDto> dto);
}
