package com.lhduc.restaurantmanagementapi.service.impl;

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
import com.lhduc.restaurantmanagementapi.util.StringUtil;
import jakarta.transaction.Transactional;
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

    /**
     * Retrieves a paginated list of menu items based on the provided filters, pagination, and sorting criteria.
     *
     * @param menuItemFilter   The filter criteria for menu items.
     * @param paginationRequest The pagination details, including offset and limit.
     * @param sortRequest       The sorting criteria for the results.
     * @return A list of {@link MenuItemDto} representing the paginated menu items.
     */
    @Override
    public List<MenuItemDto> getAll(MenuItemFilter menuItemFilter, PaginationRequest paginationRequest, SortRequest sortRequest) {
        Pageable pageRequest = PageRequest.of(paginationRequest.getOffset(), paginationRequest.getLimit(), Sort.by(sortRequest.buildSortOrders()));
        MenuItem probe = menuItemMapper.convertToEntityFromFilter(menuItemFilter);
        Page<MenuItem> menuItemPage = menuItemRepository.findAll(Example.of(probe), pageRequest);
        return menuItemMapper.convertToDto(menuItemPage.getContent());
    }

    /**
     * Retrieves a menu item by its unique identifier.
     *
     * @param menuItemId The unique identifier of the menu item.
     * @return The {@link MenuItemDto} representing the retrieved menu item.
     * @throws NotFoundException if the menu item with the given ID is not found.
     */
    @Override
    public MenuItemDto getById(int menuItemId) {
        MenuItem menuItem = this.findMenuItemByIdOrThrow(menuItemId);
        return menuItemMapper.convertToDto(menuItem);
    }

    /**
     * Creates a new menu item based on the provided creation request.
     *
     * @param menuItemCreateRequest The request containing information to create a new menu item.
     * @return The {@link MenuItemDto} representing the newly created menu item.
     */
    @Override
    public MenuItemDto create(MenuItemCreateRequest menuItemCreateRequest) {
        MenuItem menuItem = menuItemMapper.convertToEntityFromRequest(menuItemCreateRequest);
        MenuItem createdMenuItem = menuItemRepository.save(menuItem);
        return menuItemMapper.convertToDto(createdMenuItem);
    }

    /**
     * Updates an existing menu item based on the provided update request.
     *
     * @param menuItemId            The unique identifier of the menu item to be updated.
     * @param menuItemUpdateRequest The request containing information to update the menu item.
     * @throws NotFoundException if the menu item with the given ID is not found.
     */
    @Override
    public void update(int menuItemId, MenuItemUpdateRequest menuItemUpdateRequest) {
        MenuItem menuItem = this.findMenuItemByIdOrThrow(menuItemId);

        if(StringUtil.isNotNull(menuItemUpdateRequest.getName())) {
            menuItem.setName(menuItemUpdateRequest.getName());
        }
        menuItem.setPrice(menuItemUpdateRequest.getPrice());

        menuItemRepository.save(menuItem);
    }

    /**
     * Deletes a menu item by its unique identifier.
     *
     * @param menuItemId The unique identifier of the menu item to be deleted.
     * @throws OperationForbiddenException if the menu item has associated bill details and cannot be deleted.
     * @throws NotFoundException  if the menu item with the given ID is not found.
     */
    @Override
    public void deleteById(int menuItemId) {
        MenuItem menuItem = this.findMenuItemByIdOrThrow(menuItemId);

        if (menuItem.getBillDetail().isEmpty()) {
            menuItemRepository.deleteById(menuItemId);
            return;
        }

        throw new OperationForbiddenException(UNABLE_TO_DELETE_MENU_ITEM);
    }

    /**
     * Finds a {@code MenuItem} entity by its ID or throws a {@code NotFoundException}
     * if the entity is not found.
     *
     * @param menuItemId The ID of the {@code MenuItem} entity to be found.
     * @return The found {@code MenuItem} entity.
     * @throws NotFoundException If the {@code MenuItem} entity with the given ID is not found.
     */
    private MenuItem findMenuItemByIdOrThrow(int menuItemId) {
        return RepositoryUtil.findEntityByIdOrThrow(menuItemId, menuItemRepository, MENU_ITEM_NOT_FOUND);
    }
}
