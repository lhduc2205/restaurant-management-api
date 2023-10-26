package com.lhduc.restaurantmanagementapi.controller;

import com.lhduc.restaurantmanagementapi.model.dto.request.MenuItemCreateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.MenuItemUpdateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.PaginationRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.sort.SortRequest;
import com.lhduc.restaurantmanagementapi.model.dto.response.MenuItemDto;
import com.lhduc.restaurantmanagementapi.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.lhduc.restaurantmanagementapi.common.constant.UriConstant.MENU_ITEMS;

@RestController
@RequiredArgsConstructor
@RequestMapping(MENU_ITEMS)
public class MenuItemController {
    private final MenuItemService menuItemService;

    @GetMapping
    public ResponseEntity<List<MenuItemDto>> getAll(
            PaginationRequest paginationRequest,
            SortRequest sortRequest
    ) {
        List<MenuItemDto> menuItems = menuItemService.getAll(paginationRequest, sortRequest);
        return ResponseEntity.ok(menuItems);
    }

    @GetMapping("{id}")
    public ResponseEntity<MenuItemDto> getById(@PathVariable int id) {
        MenuItemDto menuItemDto = menuItemService.getById(id);
        return ResponseEntity.ok(menuItemDto);
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody MenuItemCreateRequest request) {
        menuItemService.create(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{id}")
    public void update(@PathVariable int id, @RequestBody MenuItemUpdateRequest request) {
        menuItemService.update(id, request);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable int id) {
        menuItemService.deleteById(id);
    }
}
