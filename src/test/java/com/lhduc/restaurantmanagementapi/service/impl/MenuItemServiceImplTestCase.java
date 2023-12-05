package com.lhduc.restaurantmanagementapi.service.impl;

import com.lhduc.restaurantmanagementapi.exception.NotFoundException;
import com.lhduc.restaurantmanagementapi.exception.OperationForbiddenException;
import com.lhduc.restaurantmanagementapi.model.dto.request.PaginationRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.menuitem.MenuItemCreateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.menuitem.MenuItemFilter;
import com.lhduc.restaurantmanagementapi.model.dto.request.menuitem.MenuItemUpdateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.sort.MenuItemSortRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.sort.SortRequest;
import com.lhduc.restaurantmanagementapi.model.dto.response.MenuItemDto;
import com.lhduc.restaurantmanagementapi.model.entity.BillDetail;
import com.lhduc.restaurantmanagementapi.model.entity.BillDetailPK;
import com.lhduc.restaurantmanagementapi.model.entity.MenuItem;
import com.lhduc.restaurantmanagementapi.repository.MenuItemRepository;
import com.lhduc.restaurantmanagementapi.service.MenuItemService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@Rollback(value = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MenuItemServiceImplTestCase {
    @Autowired
    private MenuItemService menuItemService;

    @Autowired
    private MenuItemRepository menuItemRepository;

    private static final String[] ITEM_NAMES = new String[]{"Bun thit nuong", "Com ga"};

    private static final int[] ITEM_PRICES = new int[]{20000, 30000};

    private static final int[] ITEM_IDS = new int[2];

    /**
     * Test case to create menu items and verify their existence in the repository.
     *
     * @see MenuItemService#create(MenuItemCreateRequest)
     */
    @Test
    @Order(1)
    void testCreateMenuItem() {
        for (int i = 0; i < 2; i++) {
            MenuItemCreateRequest menuItemCreateRequest = new MenuItemCreateRequest();
            menuItemCreateRequest.setName(ITEM_NAMES[i]);
            menuItemCreateRequest.setPrice(ITEM_PRICES[i]);

            MenuItemDto menuItem = menuItemService.create(menuItemCreateRequest);
            ITEM_IDS[i] = menuItem.getId();
        }

        for (int itemId : ITEM_IDS) {
            assertTrue(menuItemRepository.existsById(itemId));
        }

    }

    /**
     * Test case to retrieve all menu items and ensure at least two items are present.
     *
     * @see MenuItemService#getAll(MenuItemFilter, PaginationRequest, SortRequest)
     */
    @Test
    @Order(2)
    void testGetAllMenuItem() {
        List<MenuItemDto> menuItemDto = menuItemService.getAll(new MenuItemFilter(), new PaginationRequest(), new SortRequest());
        assertTrue(menuItemDto.size() >= 2);
    }

    /**
     * Test case to retrieve menu items with pagination and verify the expected number of items per page.
     *
     * @see MenuItemService#getAll(MenuItemFilter, PaginationRequest, SortRequest)
     */
    @Test
    @Order(3)
    void testGetAllMenuItemWithPagination() {
        PaginationRequest paginationRequest = new PaginationRequest();

        // Test with Offset = 0 and Limit = 1
        paginationRequest.setLimit(1);
        List<MenuItemDto> menuItemDto = menuItemService.getAll(new MenuItemFilter(), paginationRequest, new SortRequest());
        assertEquals(1, menuItemDto.size());

        // Test with Offset = 1 and Limit = 1
        paginationRequest.setOffset(1);
        paginationRequest.setLimit(1);
        menuItemDto = menuItemService.getAll(new MenuItemFilter(), paginationRequest, new SortRequest());
        assertEquals(1, menuItemDto.size());
    }

    /**
     * Test case to retrieve all menu items with descending sorting by ID and ensure proper sorting.
     *
     * @see MenuItemService#getAll(MenuItemFilter, PaginationRequest, SortRequest)
     */
    @Test
    @Order(4)
    void testGetAllMenuItemWithDescSorting() {
        MenuItemSortRequest sortRequest = new MenuItemSortRequest();
        sortRequest.setSort("-id");

        List<MenuItemDto> menuItemDto = menuItemService.getAll(new MenuItemFilter(), new PaginationRequest(), sortRequest);
        assertTrue(menuItemDto.size() >= 2);
        assertTrue(menuItemDto.get(0).getId() > menuItemDto.get(1).getId());
    }

    /**
     * Test case to retrieve menu items with filtering and verify the expected results.
     *
     * @see MenuItemService#getAll(MenuItemFilter, PaginationRequest, SortRequest)
     */
    @Test
    @Order(5)
    void testGetAllMenuItemWithFiltering() {
        MenuItemFilter menuItemFilter = new MenuItemFilter();
        menuItemFilter.setName(ITEM_NAMES[0]);

        List<MenuItemDto> menuItemDto = menuItemService.getAll(menuItemFilter, new PaginationRequest(), new SortRequest());
        assertFalse(menuItemDto.isEmpty());
        assertEquals(ITEM_NAMES[0], menuItemDto.get(0).getName());

        menuItemFilter.setName("Banh mi");
        menuItemDto = menuItemService.getAll(menuItemFilter, new PaginationRequest(), new SortRequest());
        assertEquals(0, menuItemDto.size());
    }

    /**
     * Test case to retrieve a specific menu item by its ID and ensure it is found.
     *
     * @see MenuItemService#getById(int)
     */
    @Test
    @Order(6)
    void testGetMenuItemById() {
        MenuItemDto menuItemDto = menuItemService.getById(1);
        assertNotNull(menuItemDto);
        assertEquals(1, menuItemDto.getId());
    }

    /**
     * Test case to attempt to retrieve a non-existent menu item by its ID, expecting a NotFoundException.
     *
     * @see MenuItemService#getById(int)
     */
    @Test
    @Order(6)
    void testGetMenuItemById_throwNotFoundException() {
        assertThrows(NotFoundException.class, () -> menuItemService.getById(999));
    }

    /**
     * Test case to update a menu item and verify the changes.
     *
     * @see MenuItemService#update(int, MenuItemUpdateRequest)
     */
    @Test
    @Order(8)
    void testUpdateMenuItem() {
        int menuItemIdToUpdate = ITEM_IDS[0];
        String newItemName = "Com bo";
        int newItemPrice = 50000;

        MenuItemUpdateRequest menuItemUpdateRequest = new MenuItemUpdateRequest();
        menuItemUpdateRequest.setName(newItemName);
        menuItemUpdateRequest.setPrice(newItemPrice);

        menuItemService.update(menuItemIdToUpdate, menuItemUpdateRequest);
        MenuItemDto menuItemDto = menuItemService.getById(menuItemIdToUpdate);
        assertEquals(newItemName, menuItemDto.getName());
        assertEquals(newItemPrice, menuItemDto.getPrice());
    }

    /**
     * Test case to attempt to update a non-existent menu item, expecting a NotFoundException.
     *
     * @see MenuItemService#update(int, MenuItemUpdateRequest)
     */
    @Test
    @Order(9)
    void testUpdateMenuItem_throwNotFoundException() {
        int menuItemIdToUpdate = 999;
        String newItemName = "Com bo";
        int newItemPrice = 50000;
        MenuItemUpdateRequest menuItemUpdateRequest = new MenuItemUpdateRequest();
        menuItemUpdateRequest.setName(newItemName);
        menuItemUpdateRequest.setPrice(newItemPrice);

        assertThrows(NotFoundException.class, () -> menuItemService.update(menuItemIdToUpdate, menuItemUpdateRequest));
    }

    /**
     * Test case to delete a menu item.
     *
     * @see MenuItemService#deleteById(int)
     */
    @Test
    @Transactional
    @Order(10)
    void testDeleteByIdMenuItemWithNoBillDetails() {
        List<MenuItem> menuItems = menuItemRepository.findAll();

        Optional<MenuItem> menuItem = menuItems.stream().filter(i -> i.getBillDetail().isEmpty()).findFirst();

        if (menuItem.isEmpty()) {
            return;
        }

        assertDoesNotThrow(() -> menuItemService.deleteById(menuItem.get().getId()));

        assertTrue(menuItemRepository.findById(menuItem.get().getId()).isEmpty());
    }
}