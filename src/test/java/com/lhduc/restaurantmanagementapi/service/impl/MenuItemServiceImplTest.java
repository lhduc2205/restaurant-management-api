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
import com.lhduc.restaurantmanagementapi.repository.MenuItemRepository;
import com.lhduc.restaurantmanagementapi.service.MenuItemService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Rollback(value = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MenuItemServiceImplTest {
    @Autowired
    private MenuItemService menuItemService;

    @Autowired
    private MenuItemRepository menuItemRepository;

    private static final String[] ITEM_NAMES = new String[]{"Bun thit nuong", "Com ga"};

    private static final int[] ITEM_PRICES = new int[]{20000, 30000};

    private static int[] ITEM_IDS = new int[2];

    @Test
    @Order(1)
    void createMenuItem() {
        for (int i = 0; i < 2; i++) {
            MenuItemCreateRequest menuItemCreateRequest = new MenuItemCreateRequest();
            menuItemCreateRequest.setName(ITEM_NAMES[i]);
            menuItemCreateRequest.setPrice(ITEM_PRICES[i]);

            MenuItemDto menuItem = menuItemService.create(menuItemCreateRequest);
            ITEM_IDS[i] = menuItem.getId();
        }

        for (int i = 0; i < ITEM_IDS.length; i++) {
            assertTrue(menuItemRepository.existsById(ITEM_IDS[i]));
        }

    }

    @Test
    @Order(2)
    void testGetAllMenuItem() {
        List<MenuItemDto> menuItemDto = menuItemService.getAll(new MenuItemFilter(), new PaginationRequest(), new SortRequest());
        assertTrue(menuItemDto.size() >= 2);
    }

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

    @Test
    @Order(4)
    void testGetAllMenuItemWithDescSorting() {
        MenuItemSortRequest sortRequest = new MenuItemSortRequest();
        sortRequest.setSort("-id");

        List<MenuItemDto> menuItemDto = menuItemService.getAll(new MenuItemFilter(), new PaginationRequest(), sortRequest);
        assertTrue(menuItemDto.size() >= 2);
        assertTrue(menuItemDto.get(0).getId() > menuItemDto.get(1).getId());
    }

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

    @Test
    @Order(6)
    void testGetMenuItemById() {
        MenuItemDto menuItemDto = menuItemService.getById(1);
        assertNotNull(menuItemDto);
        assertEquals(1, menuItemDto.getId());
    }

    @Test
    @Order(6)
    void testGetMenuItemById_throwNotFoundException() {
        assertThrows(NotFoundException.class, () -> menuItemService.getById(999));
    }

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

    @Test
    @Order(10)
    void deleteMenuItemById_throwOperationForbiddenException() {
        assertThrows(OperationForbiddenException.class, () -> menuItemService.deleteById(1));
    }
}