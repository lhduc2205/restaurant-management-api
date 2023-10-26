package com.lhduc.restaurantmanagementapi.service.impl;

import com.lhduc.restaurantmanagementapi.model.mappers.MenuItemCreateRequestMapper;
import com.lhduc.restaurantmanagementapi.model.dto.request.MenuItemCreateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.MenuItemUpdateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.PaginationRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.sort.SortRequest;
import com.lhduc.restaurantmanagementapi.model.dto.response.MenuItemDto;
import com.lhduc.restaurantmanagementapi.model.entity.MenuItem;
import com.lhduc.restaurantmanagementapi.repository.MenuItemRepository;
import com.lhduc.restaurantmanagementapi.service.MenuItemService;
import com.lhduc.restaurantmanagementapi.model.mappers.MenuItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final MenuItemMapper menuItemMapper;
    private final MenuItemCreateRequestMapper menuItemCreateRequestMapper;

    @Override
    public List<MenuItemDto> getAll(PaginationRequest paginationRequest, SortRequest sortRequest) {
        Pageable pageRequest = PageRequest.of(paginationRequest.getOffset(), paginationRequest.getLimit(), Sort.by(sortRequest.extractSortOrder()));

        Page<MenuItem> menuItemPage = menuItemRepository.findAll(pageRequest);
        List<MenuItem> menuItems = menuItemPage.getContent();
        return menuItemMapper.convertToDto(menuItems);
    }

    @Override
    public MenuItemDto getById(int id) {
        MenuItem menuItem = getExistedMenuItem(id);

        return menuItemMapper.convertToDto(menuItem);
    }

    @Override
    public void create(MenuItemCreateRequest request) {
        MenuItem menuItem = menuItemCreateRequestMapper.convertToEntity(request);
        menuItemRepository.save(menuItem);
    }

    @Override
    public void update(int id, MenuItemUpdateRequest request) {
        MenuItem menuItem = getExistedMenuItem(id);

        menuItem.setName(request.getName());
        menuItem.setPrice(request.getPrice());
        menuItemRepository.save(menuItem);
    }

    @Override
    public void deleteById(int id) {
        MenuItem menuItem = getExistedMenuItem(id);
        menuItemRepository.delete(menuItem);
    }

    private MenuItem getExistedMenuItem(int id) {
        return menuItemRepository.findById(id).orElseThrow();
    }


}
