package com.lhduc.restaurantmanagementapi.service;

import com.lhduc.restaurantmanagementapi.dto.request.MenuItemCreateRequest;
import com.lhduc.restaurantmanagementapi.dto.request.MenuItemUpdateRequest;
import com.lhduc.restaurantmanagementapi.dto.response.MenuItemDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MenuItemService {
    List<MenuItemDto> getAll(Pageable pageable);

    MenuItemDto getById(int id);

    void create(MenuItemCreateRequest request);

    void update(int id, MenuItemUpdateRequest request);

    void deleteById(int id);
}
