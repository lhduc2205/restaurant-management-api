package com.lhduc.restaurantmanagementapi.service.impl;

import com.lhduc.restaurantmanagementapi.common.constant.MessageConstant;
import com.lhduc.restaurantmanagementapi.exception.NotFoundException;
import com.lhduc.restaurantmanagementapi.exception.OperationForbiddenException;
import com.lhduc.restaurantmanagementapi.model.dto.request.menuitem.MenuItemCreateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.menuitem.MenuItemFilter;
import com.lhduc.restaurantmanagementapi.model.dto.request.menuitem.MenuItemUpdateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.PaginationRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.sort.SortRequest;
import com.lhduc.restaurantmanagementapi.model.dto.response.MenuItemDto;
import com.lhduc.restaurantmanagementapi.model.entity.MenuItem;
import com.lhduc.restaurantmanagementapi.repository.MenuItemRepository;
import com.lhduc.restaurantmanagementapi.service.MenuItemService;
import com.lhduc.restaurantmanagementapi.model.mappers.MenuItemMapper;
import com.lhduc.restaurantmanagementapi.util.RepositoryUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.lhduc.restaurantmanagementapi.common.constant.MessageConstant.MENU_ITEM_NOT_FOUND;
import static com.lhduc.restaurantmanagementapi.common.constant.MessageConstant.UNABLE_TO_DELETE_MENU_ITEM;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final MenuItemMapper menuItemMapper;

    @Override
    public List<MenuItemDto> getAll(MenuItemFilter menuItemFilter, PaginationRequest paginationRequest, SortRequest sortRequest) {
        Pageable pageRequest = PageRequest.of(paginationRequest.getOffset(), paginationRequest.getLimit(), Sort.by(sortRequest.buildSortOrders()));
        MenuItem probe = menuItemMapper.convertToEntityFromFilter(menuItemFilter);
        Page<MenuItem> menuItemPage = menuItemRepository.findAll(Example.of(probe), pageRequest);
        return menuItemMapper.convertToDto(menuItemPage.getContent());
    }

    @Override
    public MenuItemDto getById(int menuItemId) {
        MenuItem menuItem = this.findMenuItemByIdOrThrow(menuItemId);
        return menuItemMapper.convertToDto(menuItem);
    }

    @Override
    public MenuItemDto create(MenuItemCreateRequest menuItemCreateRequest) {
        MenuItem menuItem = menuItemMapper.convertToEntityFromRequest(menuItemCreateRequest);
        MenuItem createdMenuItem = menuItemRepository.save(menuItem);
        return menuItemMapper.convertToDto(createdMenuItem);
    }

    @Override
    public void update(int menuItemId, MenuItemUpdateRequest menuItemUpdateRequest) {
        MenuItem menuItem = this.findMenuItemByIdOrThrow(menuItemId);
        menuItem.setName(menuItemUpdateRequest.getName());
        menuItem.setPrice(menuItemUpdateRequest.getPrice());
        menuItemRepository.save(menuItem);
    }

    @Override
    public void deleteById(int menuItemId) {
        throw new OperationForbiddenException(UNABLE_TO_DELETE_MENU_ITEM);
    }

    private MenuItem findMenuItemByIdOrThrow(int menuItemId) {
        return RepositoryUtil.findEntityByIdOrThrow(menuItemId, menuItemRepository, MENU_ITEM_NOT_FOUND);
    }
}
