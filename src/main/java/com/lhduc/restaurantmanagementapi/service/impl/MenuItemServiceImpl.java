package com.lhduc.restaurantmanagementapi.service.impl;

import com.lhduc.restaurantmanagementapi.common.mappers.MenuItemCreateRequestMapper;
import com.lhduc.restaurantmanagementapi.dto.request.MenuItemCreateRequest;
import com.lhduc.restaurantmanagementapi.dto.request.MenuItemUpdateRequest;
import com.lhduc.restaurantmanagementapi.dto.response.MenuItemDto;
import com.lhduc.restaurantmanagementapi.entity.MenuItem;
import com.lhduc.restaurantmanagementapi.repository.MenuItemRepository;
import com.lhduc.restaurantmanagementapi.service.MenuItemService;
import com.lhduc.restaurantmanagementapi.common.mappers.MenuItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final MenuItemMapper menuItemMapper = MenuItemMapper.INSTANCE;
    private final MenuItemCreateRequestMapper menuItemCreateRequestMapper = MenuItemCreateRequestMapper.INSTANCE;

    @Override
    public List<MenuItemDto> getAll(Pageable pageable) {
        Page<MenuItem> menuItemPage = menuItemRepository.findAll(pageable);
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
