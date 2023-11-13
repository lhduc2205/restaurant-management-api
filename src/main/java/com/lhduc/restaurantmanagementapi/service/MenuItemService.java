package com.lhduc.restaurantmanagementapi.service;

import com.lhduc.restaurantmanagementapi.exception.NotFoundException;
import com.lhduc.restaurantmanagementapi.model.dto.request.menuitem.MenuItemCreateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.menuitem.MenuItemFilter;
import com.lhduc.restaurantmanagementapi.model.dto.request.menuitem.MenuItemUpdateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.PaginationRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.sort.SortRequest;
import com.lhduc.restaurantmanagementapi.model.dto.response.MenuItemDto;

import java.util.List;

public interface MenuItemService {
    /**
     * Retrieves list of menu items based on the provided filtering, pagination, and sorting criteria.
     *
     * @param menuItemFilter    An object specifying the filter criteria for menu items.
     * @param paginationRequest An object specifying the pagination parameters.
     * @param sortRequest       An Object specifying the sorting criteria.
     * @return A list of MenuItemDto object that match the provided criteria.
     */
    List<MenuItemDto> getAll(MenuItemFilter menuItemFilter, PaginationRequest paginationRequest, SortRequest sortRequest);


    /**
     * Retrieves a menu item by its unique ID.
     *
     * @param menuItemId The unique ID of the menu item to be retrieved.
     * @return A MenuItemDto representing the retrieved menu item.
     * @throws NotFoundException if no menu item is found with the given ID.
     */
    MenuItemDto getById(int menuItemId);

    /**
     * Creates a new item based on the provided request, which includes the name and the quantity.
     *
     * @param menuItemCreateRequest The request object containing the details for creating the menu item,
     *                              includes name and quantity.
     * @return A MenuItemDto representing the created menu item.
     */
    MenuItemDto create(MenuItemCreateRequest menuItemCreateRequest);

    /**
     * Updates the details of an existing menu item.
     *
     * @param menuItemId            The unique ID of the menu item to be updated.
     * @param menuItemUpdateRequest The request object containing details for updating the menu item,
     *                              including name and price of the menu item.
     * @throws NotFoundException if no menu item is found with the given ID.
     */
    void update(int menuItemId, MenuItemUpdateRequest menuItemUpdateRequest);

    /**
     * Deletes an existing menu item.
     *
     * @param menuItemId The unique ID of menu item to be deleted.
     * @throws NotFoundException if no menu item is found with the given ID.
     */
    void deleteById(int menuItemId);
}
