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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.lhduc.restaurantmanagementapi.common.constant.MessageConstant.UNABLE_TO_DELETE_MENU_ITEM;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final MenuItemMapper menuItemMapper;

    @Override
    public List<MenuItemDto> getAll(MenuItemFilter menuItemFilter, PaginationRequest paginationRequest, SortRequest sortRequest) {
        Pageable pageRequest = PageRequest.of(paginationRequest.getOffset(), paginationRequest.getLimit(), Sort.by(sortRequest.extractSortOrder()));
        MenuItem probe = menuItemMapper.convertToEntityFromFilter(menuItemFilter);

        Page<MenuItem> menuItemPage = menuItemRepository.findAll(Example.of(probe), pageRequest);
        return menuItemMapper.convertToDto(menuItemPage.getContent());
    }

    @Override
    public MenuItemDto getById(int id) {
        MenuItem menuItem = this.getExistedMenuItem(id);

        return menuItemMapper.convertToDto(menuItem);
    }

    @Override
    public void create(MenuItemCreateRequest request) {
        MenuItem menuItem = menuItemMapper.convertToEntityFromRequest(request);
        menuItemRepository.save(menuItem);
    }

    @Override
    public void update(int id, MenuItemUpdateRequest request) {
        MenuItem menuItem = this.getExistedMenuItem(id);

        menuItem.setName(request.getName());
        menuItem.setPrice(request.getPrice());
        menuItemRepository.save(menuItem);
    }

    @Override
    public void deleteById(int id) {
        throw new OperationForbiddenException(UNABLE_TO_DELETE_MENU_ITEM);
    }

    private MenuItem getExistedMenuItem(int id) {
        return menuItemRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(MessageConstant.MENU_ITEM_NOT_FOUND));
    }
}
