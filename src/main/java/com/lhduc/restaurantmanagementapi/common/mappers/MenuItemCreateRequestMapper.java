package com.lhduc.restaurantmanagementapi.common.mappers;

import com.lhduc.restaurantmanagementapi.dto.request.MenuItemCreateRequest;
import com.lhduc.restaurantmanagementapi.entity.MenuItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MenuItemCreateRequestMapper {
    MenuItemCreateRequestMapper INSTANCE = Mappers.getMapper(MenuItemCreateRequestMapper.class);

    MenuItem convertToEntity(MenuItemCreateRequest request);
}
