package com.lhduc.restaurantmanagementapi.controller;

import com.lhduc.restaurantmanagementapi.exception.NotFoundException;
import com.lhduc.restaurantmanagementapi.model.dto.request.menuitem.MenuItemCreateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.menuitem.MenuItemFilter;
import com.lhduc.restaurantmanagementapi.model.dto.request.menuitem.MenuItemUpdateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.PaginationRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.sort.MenuItemSortRequest;
import com.lhduc.restaurantmanagementapi.model.dto.response.MenuItemDto;
import com.lhduc.restaurantmanagementapi.model.dto.response.SuccessResponse;
import com.lhduc.restaurantmanagementapi.service.MenuItemService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

import static com.lhduc.restaurantmanagementapi.common.constant.MessageConstant.GET_ALL_MENU_ITEM_SUCCESSFULLY;
import static com.lhduc.restaurantmanagementapi.common.constant.MessageConstant.GET_MENU_ITEM_BY_ID_SUCCESSFULLY;
import static com.lhduc.restaurantmanagementapi.common.constant.UriConstant.MENU_ITEMS_ENDPOINT;

@RestController
@RequiredArgsConstructor
@RequestMapping(MENU_ITEMS_ENDPOINT)
public class MenuItemController {
    private final MenuItemService menuItemService;
    private final Logger logger = LoggerFactory.getLogger(MenuItemController.class);

    /**
     * Retrieves list of menu items based on the provided filtering, pagination, and sorting criteria.
     *
     * @param menuItemFilter    An object specifying the filter criteria for menu items.
     * @param paginationRequest An object specifying the pagination parameters.
     * @param sort              An Object specifying the sorting criteria.
     * @return A ResponseEntity containing a SuccessResponse with a list of MenuItemDto objects.
     * A SuccessResponse indicates that the operation was successful,
     * contains the menu items that match the provided criteria.
     * @throws ValidationException if the input parameters fail validation.
     */
    @GetMapping
    public ResponseEntity<SuccessResponse<List<MenuItemDto>>> getAll(
            @Nullable @Valid MenuItemFilter menuItemFilter,
            @Nullable @Valid PaginationRequest paginationRequest,
            @Nullable @Valid MenuItemSortRequest sort
    ) {
        List<MenuItemDto> menuItems = menuItemService.getAll(menuItemFilter, paginationRequest, sort);
        logger.info("Get all menu items");
        return ResponseEntity.ok(SuccessResponse.of(menuItems, GET_ALL_MENU_ITEM_SUCCESSFULLY));
    }

    /**
     * Retrieves a menu item by its unique ID.
     *
     * @param menuItemId The unique ID of the menu item to be retrieved.
     * @return A ResponseEntity containing a SuccessResponse with MenuItemDto object.
     * @throws NotFoundException if no menu item is found with the given ID.
     */
    @GetMapping("{menuItemId}")
    public ResponseEntity<SuccessResponse<MenuItemDto>> getById(@PathVariable int menuItemId) {
        MenuItemDto menuItemDto = menuItemService.getById(menuItemId);
        logger.info("Get menu item with id = {}", menuItemId);
        return ResponseEntity.ok(SuccessResponse.of(menuItemDto, GET_MENU_ITEM_BY_ID_SUCCESSFULLY));
    }

    /**
     * Creates new menu item based on the provided request, which includes the name and the quantity.
     *
     * @param menuItemCreateRequest The request object containing the details for creating the menu item,
     *                              includes name and quantity.
     * @return A ResponseEntity containing URI for the newly created resource.
     * @throws ValidationException if the input parameters fail validation.
     */
    @PostMapping
    public ResponseEntity<URI> create(@RequestBody @Valid MenuItemCreateRequest menuItemCreateRequest) {
        final MenuItemDto menuItem = menuItemService.create(menuItemCreateRequest);
        logger.info("Create menu item with request: {}", menuItemCreateRequest);
        return ResponseEntity.created(URI.create(MENU_ITEMS_ENDPOINT + "/" + menuItem.getId())).build();
    }

    /**
     * Updates the details of an existing menu item.
     *
     * @param menuItemId            The unique ID of the menu item to be updated.
     * @param menuItemUpdateRequest The request object containing details for updating the menu item,
     *                              including name and price of the menu item.
     * @return A ResponseEntity indicating the successful update of the menu item with a status of "No content".
     * @throws NotFoundException   if no menu item is found with the given ID.
     * @throws ValidationException if the input parameters fail validation.
     */
    @PutMapping("{menuItemId}")
    public ResponseEntity<Void> update(@PathVariable int menuItemId, @RequestBody @Valid MenuItemUpdateRequest menuItemUpdateRequest) {
        menuItemService.update(menuItemId, menuItemUpdateRequest);
        logger.info("Update menu item with id = {} and request = {}", menuItemId, menuItemUpdateRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Deletes an existing menu item.
     *
     * @param menuItemId The unique ID of menu item to be deleted.
     * @return A ResponseEntity indicating the successful deletion of the menu item with a status of "No content".
     * @throws NotFoundException if no menu item is found with the given ID.
     */
    @DeleteMapping("{menuItemId}")
    public ResponseEntity<Void> delete(@PathVariable int menuItemId) {
        menuItemService.deleteById(menuItemId);
        logger.info("Delete menu item with id = {}", menuItemId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
