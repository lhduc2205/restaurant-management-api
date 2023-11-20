package com.lhduc.restaurantmanagementapi.util;

import com.lhduc.restaurantmanagementapi.exception.NotFoundException;
import com.lhduc.restaurantmanagementapi.model.entity.MenuItem;
import com.lhduc.restaurantmanagementapi.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.lhduc.restaurantmanagementapi.common.constant.MessageConstant.MENU_ITEM_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class MenuItemRepositoryUtil {
    private final MenuItemRepository repository;

    /**
     * Finds a {@code MenuItem} entity by its ID or throws a {@code NotFoundException}
     * if the entity is not found.
     *
     * @param menuItemId The ID of the {@code MenuItem} entity to be found.
     * @return The found {@code MenuItem} entity.
     * @throws NotFoundException If the {@code MenuItem} entity with the given ID is not found.
     */
    public MenuItem findByIdOrThrow(int menuItemId) {
        return RepositoryUtil.findEntityByIdOrThrow(menuItemId, repository, MENU_ITEM_NOT_FOUND);
    }
}
