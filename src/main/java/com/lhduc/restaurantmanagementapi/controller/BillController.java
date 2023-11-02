package com.lhduc.restaurantmanagementapi.controller;

import com.lhduc.restaurantmanagementapi.model.dto.request.bill.AddMoreItemToBillRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillCreateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.PaginationRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillDetailUpdateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillFilter;
import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillUpdateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.sort.BillSortRequest;
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
            @Valid BillFilter billFilter,
            @Valid PaginationRequest paginationRequest,
            @Valid BillSortRequest sort
    ) {
        List<BillDto> bills = billService.getAllBill(billFilter, paginationRequest, sort);
        return ResponseEntity.ok(SuccessResponse.of(bills, GET_ALL_BILL_SUCCESSFULLY));
    }

    @GetMapping("{billId}")
    public ResponseEntity<SuccessResponse<BillDto>> getById(@PathVariable("billId") int billId) {
        BillDto bill = billService.getBillById(billId);
        return ResponseEntity.ok(SuccessResponse.of(bill, GET_BILL_BY_ID_SUCCESSFULLY));
    }

    @PostMapping
    public ResponseEntity<URI> createBill(@RequestBody @Valid BillCreateRequest billRequest) {
        billService.createBill(billRequest);

        return ResponseEntity.created(URI.create(BILLS_ENDPOINT + "/" + 1)).build();
    }

    @PostMapping("{billId}/items")
    public ResponseEntity<URI> addMoreBillItems(
            @PathVariable("billId") int billId,
            @RequestBody @Valid AddMoreItemToBillRequest billDetailsRequest
    ) {
        billService.addMoreBillItems(billId, billDetailsRequest.getItems());

        return ResponseEntity.created(URI.create(BILLS_ENDPOINT)).build();
    }

    @PutMapping("{billId}")
    public ResponseEntity<Void> updateBill(
            @PathVariable("billId") int billId,
            @RequestBody @Valid BillUpdateRequest billUpdateRequest
    ) {
        billService.updateBill(billId, billUpdateRequest);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("{billId}/items")
    public ResponseEntity<Void> updateBillItems(
            @PathVariable("billId") int billId,
            @RequestBody @Valid List<BillDetailUpdateRequest> billDetailUpdateRequest
    ) {
        billService.updateBillItems(billId, billDetailUpdateRequest);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("{billId}")
    public ResponseEntity<Void> deleteBill(@PathVariable("billId") int billId) {
        billService.deleteBillById(billId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("{billId}/items/{menuItemId}")
    public ResponseEntity<Void> deleteBillItem(
            @PathVariable("billId") int billId,
            @PathVariable("menuItemId") int menuItemId
    ) {
        billService.deleteBillItem(billId, menuItemId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
