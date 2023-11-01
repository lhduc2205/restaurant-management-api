package com.lhduc.restaurantmanagementapi.repository;

import com.lhduc.restaurantmanagementapi.model.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Integer> {
}
