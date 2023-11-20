package com.lhduc.restaurantmanagementapi.repository;

import com.lhduc.restaurantmanagementapi.exception.NotFoundException;
import com.lhduc.restaurantmanagementapi.model.entity.BillDetail;
import com.lhduc.restaurantmanagementapi.model.entity.BillDetailPK;
import org.springframework.data.jpa.repository.JpaRepository;

import static com.lhduc.restaurantmanagementapi.common.constant.MessageConstant.BILL_DETAIL_NOT_FOUND;

public interface BillDetailRepository extends JpaRepository<BillDetail, BillDetailPK> {
    default BillDetail findByIdOrThrow(int billId, int menuItemId) {
        return this.findById(new BillDetailPK(billId, menuItemId)).orElseThrow(() -> new NotFoundException(BILL_DETAIL_NOT_FOUND));
    }
}
