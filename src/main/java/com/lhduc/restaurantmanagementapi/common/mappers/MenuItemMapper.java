package com.lhduc.restaurantmanagementapi.common.mappers;

import com.lhduc.restaurantmanagementapi.dto.response.MenuItemDto;
import com.lhduc.restaurantmanagementapi.entity.MenuItem;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

@Mapper
public interface MenuItemMapper extends BaseMapper<MenuItemDto, MenuItem> {
    MenuItemMapper INSTANCE = Mappers.getMapper(MenuItemMapper.class);

    @Override
    MenuItemDto convertToDto(MenuItem menuItem);

    @Override
    List<MenuItemDto> convertToDto(Collection<MenuItem> entity);

    @Override
    MenuItem convertToEntity(MenuItemDto menuItemDto);

    @Override
    List<MenuItem> convertToEntity(Collection<MenuItemDto> dto);
}
