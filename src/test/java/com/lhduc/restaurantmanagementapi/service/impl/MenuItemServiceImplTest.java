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
import com.lhduc.restaurantmanagementapi.service.MenuItemService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Rollback(value = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MenuItemServiceImplTest {
    @Autowired
    private MenuItemService menuItemService;

    private static final String[] ITEM_NAMES = new String[]{"Bun bo", "Com ga"};
    private static final int[] ITEM_PRICES = new int[]{20000, 30000};

    @Test
    @Order(1)
    void createMenuItem() {
        for (int i = 0; i < 2; i++) {
            MenuItemCreateRequest menuItemCreateRequest = new MenuItemCreateRequest();
            menuItemCreateRequest.setName(ITEM_NAMES[i]);
            menuItemCreateRequest.setPrice(ITEM_PRICES[i]);

            MenuItemDto menuItemDto = menuItemService.create(menuItemCreateRequest);

            assertNotNull(menuItemDto);
            assertEquals(i + 1, menuItemDto.getId());
            assertEquals(ITEM_NAMES[i], menuItemDto.getName());
            assertEquals(ITEM_PRICES[i], menuItemDto.getPrice());
        }
    }

    @Test
    @Order(2)
    void testGetAllMenuItem() {
        List<MenuItemDto> menuItemDto = menuItemService.getAll(new MenuItemFilter(), new PaginationRequest(), new SortRequest());
        assertEquals(2, menuItemDto.size());

        for (int i = 0; i < 2; i++) {
            assertEquals(ITEM_NAMES[i], menuItemDto.get(i).getName());
            assertEquals(ITEM_PRICES[i], menuItemDto.get(i).getPrice());
        }
    }

    @Test
    @Order(3)
    void testGetAllMenuItemWithPagination() {
        PaginationRequest paginationRequest = new PaginationRequest();

        // Test with Offset = 0 and Limit = 1
        paginationRequest.setLimit(1);
        List<MenuItemDto> menuItemDto = menuItemService.getAll(new MenuItemFilter(), paginationRequest, new SortRequest());
        assertEquals(1, menuItemDto.size());
        assertEquals("Bun bo", menuItemDto.get(0).getName());
        assertEquals(20000, menuItemDto.get(0).getPrice());

        // Test with Offset = 1 and Limit = 1
        paginationRequest.setOffset(1);
        paginationRequest.setLimit(1);
        menuItemDto = menuItemService.getAll(new MenuItemFilter(), paginationRequest, new SortRequest());
        assertEquals(1, menuItemDto.size());
        assertEquals("Com ga", menuItemDto.get(0).getName());
        assertEquals(30000, menuItemDto.get(0).getPrice());
    }

    @Test
    @Order(4)
    void testGetAllMenuItemWithDescSorting() {
        MenuItemSortRequest sortRequest = new MenuItemSortRequest();
        sortRequest.setSort("-id");

        List<MenuItemDto> menuItemDto = menuItemService.getAll(new MenuItemFilter(), new PaginationRequest(), sortRequest);
        assertEquals(2, menuItemDto.size());
        assertEquals(2, menuItemDto.get(0).getId());
        assertEquals(1, menuItemDto.get(1).getId());
    }

    @Test
    @Order(5)
    void testGetAllMenuItemWithFiltering() {
        MenuItemFilter menuItemFilter = new MenuItemFilter();
        menuItemFilter.setName("Bun bo");

        List<MenuItemDto> menuItemDto = menuItemService.getAll(menuItemFilter, new PaginationRequest(), new SortRequest());
        assertEquals(1, menuItemDto.size());
        assertEquals("Bun bo", menuItemDto.get(0).getName());

        menuItemFilter.setName("Com tam");
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
        int menuItemIdToUpdate = 1;
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