package com.lhduc.restaurantmanagementapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lhduc.restaurantmanagementapi.exception.NotFoundException;
import com.lhduc.restaurantmanagementapi.exception.OperationForbiddenException;
import com.lhduc.restaurantmanagementapi.model.dto.request.menuitem.MenuItemCreateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.menuitem.MenuItemUpdateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.response.MenuItemDto;
import com.lhduc.restaurantmanagementapi.service.MenuItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.List;

import static com.lhduc.restaurantmanagementapi.common.constant.UriConstant.MENU_ITEMS_ENDPOINT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class MenuItemControllerTestCase {
    @Autowired
    private MockMvc mvcMock;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuItemService menuItemService;

    private static final MenuItemDto MENU_ITEM_1 = new MenuItemDto(1, "Bun bo", 20000);
    private static final MenuItemDto MENU_ITEM_2 = new MenuItemDto(2, "Banh mi", 15000);

    /**
     * Tests the successful retrieval of all menu items.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    void testGetAllMenuItem() throws Exception {
        final List<MenuItemDto> items = List.of(MENU_ITEM_1, MENU_ITEM_2);
        when(menuItemService.getAll(any(), any(), any())).thenReturn(items);

        mvcMock.perform(get(MENU_ITEMS_ENDPOINT))
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * Tests the successful retrieval of all menu items with descending sorting by ID.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    void testGetAllMenuItemWithDescSorting() throws Exception {
        final List<MenuItemDto> items = List.of(MENU_ITEM_2, MENU_ITEM_1);
        when(menuItemService.getAll(any(), any(), any())).thenReturn(items);

        RequestBuilder requestBuilder = get(MENU_ITEMS_ENDPOINT)
                .param("sort", "-id");

        mvcMock.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * Tests the attempt to retrieve all menu items with an incorrect sorting field, expecting a Bad Request response.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    void testGetAllMenuItemWithWrongSortingField() throws Exception {
        RequestBuilder requestBuilder = get(MENU_ITEMS_ENDPOINT)
                .param("sort", "ids");

        mvcMock.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(menuItemService, never()).getAll(any(), any(), any());
    }

    /**
     * Tests the successful retrieval of a menu item by its ID.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    void testGetMenuItemById() throws Exception {
        when(menuItemService.getById(anyInt())).thenReturn(MENU_ITEM_1);
        mvcMock.perform(get(MENU_ITEMS_ENDPOINT + "/{id}", anyInt()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * Tests the attempt to retrieve a non-existent menu item by its ID, expecting a Not Found response.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    void testGetMenuItemById_ThrowNotFoundException() throws Exception {
        int notFoundId = 999;
        when(menuItemService.getById(notFoundId)).thenThrow(NotFoundException.class);
        mvcMock.perform(get(MENU_ITEMS_ENDPOINT + "/{id}", notFoundId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    /**
     * Tests the successful creation of a new menu item.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    void testCreateMenuItem() throws Exception {
        MenuItemCreateRequest menuItemCreateRequest = new MenuItemCreateRequest();
        menuItemCreateRequest.setName(MENU_ITEM_1.getName());
        menuItemCreateRequest.setPrice(MENU_ITEM_1.getPrice());

        when(menuItemService.create(any())).thenReturn(MENU_ITEM_1);

        RequestBuilder requestBuilder = post(MENU_ITEMS_ENDPOINT)
                .content(objectMapper.writeValueAsString(menuItemCreateRequest))
                .contentType(MediaType.APPLICATION_JSON);

        mvcMock.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated());
    }

    /**
     * Tests the successful update of a menu item.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    void testUpdateMenuItem() throws Exception {
        MenuItemUpdateRequest menuItemUpdateRequest = new MenuItemUpdateRequest();
        menuItemUpdateRequest.setName("Bun dau");
        menuItemUpdateRequest.setPrice(20000);

        RequestBuilder requestBuilder = put(MENU_ITEMS_ENDPOINT + "/{id}", MENU_ITEM_1.getId())
                .content(objectMapper.writeValueAsString(menuItemUpdateRequest))
                .contentType(MediaType.APPLICATION_JSON);

        mvcMock.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(menuItemService).update(MENU_ITEM_1.getId(), menuItemUpdateRequest);
    }

    /**
     * Tests the attempt to update a non-existent menu item, expecting a Not Found response.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    void testUpdateMenuItem_ThrowNotFoundException() throws Exception {
        int notFoundId = 999;
        MenuItemUpdateRequest menuItemUpdateRequest = new MenuItemUpdateRequest();
        menuItemUpdateRequest.setName("Bun dau");
        menuItemUpdateRequest.setPrice(20000);

        doThrow(NotFoundException.class).when(menuItemService).update(notFoundId, menuItemUpdateRequest);

        RequestBuilder requestBuilder = put(MENU_ITEMS_ENDPOINT + "/{id}", notFoundId)
                .content(objectMapper.writeValueAsString(menuItemUpdateRequest))
                .contentType(MediaType.APPLICATION_JSON);

        mvcMock.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(menuItemService).update(notFoundId, menuItemUpdateRequest);
    }

    /**
     * Tests the successful deletion of an existent menu item with no bill details.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    void testDeleteMenuItemWithNoBillDetails() throws Exception {
        RequestBuilder requestBuilder = delete(MENU_ITEMS_ENDPOINT + "/{id}", 1);

        mvcMock.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(menuItemService).deleteById(anyInt());
    }
}