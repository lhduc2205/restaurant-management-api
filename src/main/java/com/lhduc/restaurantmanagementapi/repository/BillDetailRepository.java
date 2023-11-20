package com.lhduc.restaurantmanagementapi.repository;

import com.lhduc.restaurantmanagementapi.model.entity.BillDetail;
import com.lhduc.restaurantmanagementapi.model.entity.BillDetailPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillDetailRepository extends JpaRepository<BillDetail, BillDetailPK> {
}
