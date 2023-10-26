package com.lhduc.restaurantmanagementapi.model.mappers;

import com.lhduc.restaurantmanagementapi.model.dto.request.MenuItemCreateRequest;
import com.lhduc.restaurantmanagementapi.model.entity.MenuItem;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MenuItemCreateRequestMapper {

    MenuItem convertToEntity(MenuItemCreateRequest request);
}
