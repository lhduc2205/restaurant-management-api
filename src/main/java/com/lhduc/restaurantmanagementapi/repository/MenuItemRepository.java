package com.lhduc.restaurantmanagementapi.repository;

import com.lhduc.restaurantmanagementapi.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
}
