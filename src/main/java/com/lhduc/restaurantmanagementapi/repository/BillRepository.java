package com.lhduc.restaurantmanagementapi.repository;

import com.lhduc.restaurantmanagementapi.exception.NotFoundException;
import com.lhduc.restaurantmanagementapi.model.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import static com.lhduc.restaurantmanagementapi.common.constant.MessageConstant.BILL_NOT_FOUND;

public interface BillRepository extends JpaRepository<Bill, Integer> {
    default Bill findByIdOrThrow(Integer id) {
        return this.findById(id).orElseThrow(() -> new NotFoundException(BILL_NOT_FOUND));
    }
}
