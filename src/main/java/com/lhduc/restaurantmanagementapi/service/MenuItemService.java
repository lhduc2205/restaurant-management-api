package com.lhduc.restaurantmanagementapi.service;

import com.lhduc.restaurantmanagementapi.model.dto.request.menuitem.MenuItemCreateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.menuitem.MenuItemUpdateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.PaginationRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.sort.SortRequest;
import com.lhduc.restaurantmanagementapi.model.dto.response.MenuItemDto;

import java.util.List;

public interface MenuItemService {
    List<MenuItemDto> getAll(PaginationRequest paginationRequest, SortRequest sortRequest);

    MenuItemDto getById(int id);

    void create(MenuItemCreateRequest request);

    void update(int id, MenuItemUpdateRequest request);

    void deleteById(int id);
}
