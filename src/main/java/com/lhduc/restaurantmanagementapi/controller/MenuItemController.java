package com.lhduc.restaurantmanagementapi.controller;

import com.lhduc.restaurantmanagementapi.dto.request.MenuItemCreateRequest;
import com.lhduc.restaurantmanagementapi.dto.request.MenuItemUpdateRequest;
import com.lhduc.restaurantmanagementapi.dto.request.PaginationRequest;
import com.lhduc.restaurantmanagementapi.dto.response.MenuItemDto;
import com.lhduc.restaurantmanagementapi.service.MenuItemService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

import static com.lhduc.restaurantmanagementapi.common.constant.UriConstant.MENU_ITEMS;

@RestController
@RequiredArgsConstructor
@RequestMapping(MENU_ITEMS)
public class MenuItemController {
    private final MenuItemService menuItemService;

    @GetMapping
    public ResponseEntity<List<MenuItemDto>> getAll(
            @RequestParam(name = "offset", defaultValue = "0") int offset,
            @RequestParam(name = "limit", defaultValue = "100") int limit
    ) {
        Pageable page = PageRequest.of(offset, limit, Sort.by("id"));

        List<MenuItemDto> menuItems = menuItemService.getAll(page);
        return ResponseEntity.ok(menuItems);
    }

    @GetMapping("{id}")
    public ResponseEntity<MenuItemDto> getById(@PathVariable int id) {
        MenuItemDto menuItemDto = menuItemService.getById(id);
        return ResponseEntity.ok(menuItemDto);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody MenuItemCreateRequest request) {
        menuItemService.create(request);
        return ResponseEntity.created(URI.create("Success")).build();
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
