package com.lhduc.restaurantmanagementapi.controller;

import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillCreateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillDetailCreateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.PaginationRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillDetailUpdateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillUpdateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.sort.BillSort;
import com.lhduc.restaurantmanagementapi.model.dto.response.BillDto;
import com.lhduc.restaurantmanagementapi.model.dto.response.SuccessResponse;
import com.lhduc.restaurantmanagementapi.service.BillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

import static com.lhduc.restaurantmanagementapi.common.constant.MessageConstant.GET_ALL_BILL_SUCCESSFULLY;
import static com.lhduc.restaurantmanagementapi.common.constant.MessageConstant.GET_BILL_BY_ID_SUCCESSFULLY;
import static com.lhduc.restaurantmanagementapi.common.constant.UriConstant.BILLS_ENDPOINT;

@RestController
@RequiredArgsConstructor
@RequestMapping(BILLS_ENDPOINT)
public class BillController {
    private final BillService billService;

    @GetMapping
    public ResponseEntity<SuccessResponse<List<BillDto>>> getAll(
            @Valid PaginationRequest paginationRequest,
            @Valid BillSort sort
    ) {
        List<BillDto> bills = billService.getAllBill(paginationRequest, sort);
        return ResponseEntity.ok(SuccessResponse.of(bills, GET_ALL_BILL_SUCCESSFULLY));
    }

    @GetMapping("{billId}")
    public ResponseEntity<SuccessResponse<BillDto>> getById(@PathVariable("billId") int billId) {
        BillDto bill = billService.getBillById(billId);
        return ResponseEntity.ok(SuccessResponse.of(bill, GET_BILL_BY_ID_SUCCESSFULLY));
    }

    @PostMapping
    public ResponseEntity<URI> createBill(@RequestBody BillCreateRequest billRequest) {
        billService.createBill(billRequest);

        return ResponseEntity.created(URI.create(BILLS_ENDPOINT + "/" + 1)).build();
    }

    @PostMapping("{billId}/details")
    public ResponseEntity<URI> addMoreBillDetails(
            @PathVariable("billId") int billId,
            @RequestBody List<BillDetailCreateRequest> billDetailsRequest
    ) {
        billService.addMoreBillDetails(billId, billDetailsRequest);

        return ResponseEntity.created(URI.create(BILLS_ENDPOINT)).build();
    }

    @PutMapping("{billId}")
    public ResponseEntity<Void> updateBill(
            @PathVariable("billId") int billId,
            @Valid @RequestBody BillUpdateRequest billUpdateRequest
    ) {
        billService.updateBill(billId, billUpdateRequest);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("{billId}/details")
    public ResponseEntity<Void> updateBillDetails(
            @PathVariable("billId") int billId,
            @Valid @RequestBody List<BillDetailUpdateRequest> billDetailUpdateRequest
    ) {
        billService.updateBillDetails(billId, billDetailUpdateRequest);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("{billId}")
    public ResponseEntity<Void> deleteBill(@PathVariable("billId") int billId) {
        billService.deleteBillById(billId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("{billId}/details/menu-items/{menuItemId}")
    public ResponseEntity<Void> deleteBillDetail(
            @PathVariable("billId") int billId,
            @PathVariable("menuItemId") int menuItemId
    ) {
        billService.deleteBillDetail(billId, menuItemId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
