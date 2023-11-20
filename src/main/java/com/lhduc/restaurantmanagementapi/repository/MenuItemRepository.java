package com.lhduc.restaurantmanagementapi.repository;

import com.lhduc.restaurantmanagementapi.exception.NotFoundException;
import com.lhduc.restaurantmanagementapi.model.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import static com.lhduc.restaurantmanagementapi.common.constant.MessageConstant.MENU_ITEM_NOT_FOUND;

public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
    default MenuItem findByIdOrThrow(int menuItemId) {
        return this.findById(menuItemId).orElseThrow(() -> new NotFoundException(MENU_ITEM_NOT_FOUND));
    }
}
