package com.lhduc.restaurantmanagementapi.service;

import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillCreateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillDetailCreateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillDetailUpdateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillUpdateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.PaginationRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.sort.SortRequest;
import com.lhduc.restaurantmanagementapi.model.dto.response.BillDto;

import java.util.List;

public interface BillService {
    List<BillDto> getAllBill(PaginationRequest paginationRequest, SortRequest sortRequest);

    BillDto getBillById(int billId);

    void createBill(BillCreateRequest billRequest);

    void addMoreBillDetails(int billId, List<BillDetailCreateRequest> billDetailsRequest);

    void updateBill(int billId, BillUpdateRequest billRequest);

    void updateBillDetails(int billId, List<BillDetailUpdateRequest> billDetailsRequest);

    void deleteBillById(int billId);

    void deleteBillDetail(int billId, int menuItem);
}
