package com.lhduc.restaurantmanagementapi.service;

import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillCreateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillDetailCreateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillDetailUpdateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillFilter;
import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillUpdateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.PaginationRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.sort.SortRequest;
import com.lhduc.restaurantmanagementapi.model.dto.response.BillDto;

import java.util.List;

public interface BillService {
    List<BillDto> getAllBill(BillFilter billFilter, PaginationRequest paginationRequest, SortRequest sortRequest);

    BillDto getBillById(int billId);

    void createBill(BillCreateRequest billRequest);

    void addMoreBillItems(int billId, List<BillDetailCreateRequest> billDetailsRequest);

    void updateBill(int billId, BillUpdateRequest billRequest);

    void updateBillItems(int billId, List<BillDetailUpdateRequest> billDetailsRequest);

    void deleteBillById(int billId);

    void deleteBillItem(int billId, int menuItem);
}
