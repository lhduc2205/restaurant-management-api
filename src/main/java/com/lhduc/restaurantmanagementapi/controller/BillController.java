package com.lhduc.restaurantmanagementapi.controller;

import com.lhduc.restaurantmanagementapi.exception.NotFoundException;
import com.lhduc.restaurantmanagementapi.exception.OperationForbiddenException;
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
import jakarta.validation.ValidationException;
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

    /**
     * Retrieves a list of bills based on the provided filtering, pagination, and sorting criteria.
     *
     * @param billFilter        An object specifying the filter criteria for bills.
     * @param paginationRequest An object specifying the pagination parameters.
     * @param sort              An object specifying the sorting criteria.
     * @return A ResponseEntity containing a SuccessResponse with a list of BillDto objects.
     * A SuccessResponse indicates that the operation was successful,
     * contains the bills that match the provided criteria.
     * @throws ValidationException if the input parameters fail validation.
     */
    @GetMapping
    public ResponseEntity<SuccessResponse<List<BillDto>>> getAll(
            @Valid BillFilter billFilter,
            @Valid PaginationRequest paginationRequest,
            @Valid BillSortRequest sort
    ) {
        List<BillDto> bills = billService.getAllBill(billFilter, paginationRequest, sort);
        return ResponseEntity.ok(SuccessResponse.of(bills, GET_ALL_BILL_SUCCESSFULLY));
    }


    /**
     * Retrieves a bill by its unique ID.
     *
     * @param billId The unique ID of the bill to be retrieved.
     * @return A ResponseEntity containing a SuccessResponse with BillDto object.
     * @throws NotFoundException if no bill is found with the given ID.
     */
    @GetMapping("{billId}")
    public ResponseEntity<SuccessResponse<BillDto>> getById(@PathVariable("billId") int billId) {
        BillDto bill = billService.getBillById(billId);
        return ResponseEntity.ok(SuccessResponse.of(bill, GET_BILL_BY_ID_SUCCESSFULLY));
    }

    /**
     * Creates new bill based on the provided request, which includes a list of items and their quantities.
     *
     * @param billRequest The request object containing the details for creating the bill,
     *                    include items and quantities.
     * @return A ResponseEntity containing a URI for the newly created resource.
     * @throws ValidationException if the input parameters fail validation.
     */
    @PostMapping
    public ResponseEntity<URI> createBill(@RequestBody @Valid BillCreateRequest billRequest) {
        final BillDto bill = billService.createBill(billRequest);
        return ResponseEntity.created(URI.create(BILLS_ENDPOINT + "/" + bill.getId())).build();
    }


    /**
     * Adds more items to an existing bill.
     *
     * @param billId             The unique ID of the bill to which items should be added.
     * @param billDetailsRequest The request object containing the details of items to add to the bill.
     * @return A ResponseEntity containing a URI for the newly created resource.
     * @throws NotFoundException           if no bill is found with the given ID.
     * @throws OperationForbiddenException if the bill has paid or cancelled with the given ID.
     */
    @PostMapping("{billId}/items")
    public ResponseEntity<URI> addMoreBillItems(
            @PathVariable("billId") int billId,
            @RequestBody @Valid AddMoreItemToBillRequest billDetailsRequest
    ) {
        billService.addMoreBillItems(billId, billDetailsRequest.getItems());
        return ResponseEntity.created(URI.create(BILLS_ENDPOINT + "/" + billId)).build();
    }

    /**
     * Updates the status or items of an existing bill.
     *
     * @param billId            The unique ID of the bill to be updated.
     * @param billUpdateRequest The request object containing the details for updating the bill,
     *                          including item or status of the bill.
     * @return A ResponseEntity indicating the successful update of the bill with a status of "No content"
     * @throws NotFoundException           if no bill or item is found with the given ID.
     * @throws ValidationException         if the input parameters fail validation.
     * @throws OperationForbiddenException if the bill has paid or cancelled with the given ID.
     */
    @PutMapping("{billId}")
    public ResponseEntity<Void> updateBill(
            @PathVariable("billId") int billId,
            @RequestBody @Valid BillUpdateRequest billUpdateRequest
    ) {
        billService.updateBill(billId, billUpdateRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Updates items of an existing bill.
     *
     * @param billId                  The unique ID of the bill to be updated.
     * @param billDetailUpdateRequest The request object containing the details for updating the bill items
     * @return A ResponseEntity indicating the successful update with a status of "No content"
     * @throws NotFoundException           if no bill or item is found with the given ID.
     * @throws ValidationException         if the input parameters fail validation.
     * @throws OperationForbiddenException if the bill has paid or cancelled with the given ID.
     */
    @PutMapping("{billId}/items")
    public ResponseEntity<Void> updateBillItems(
            @PathVariable("billId") int billId,
            @RequestBody @Valid List<BillDetailUpdateRequest> billDetailUpdateRequest
    ) {
        billService.updateBillItems(billId, billDetailUpdateRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Deletes an existing bill.
     *
     * @param billId The unique ID of the bill to be deleted.
     * @return A ResponseEntity indicating the successful deletion with a status of "No content"
     * @throws NotFoundException           if no bill or item is found with the given ID.
     * @throws OperationForbiddenException if the bill has paid or cancelled with the given ID.
     */
    @DeleteMapping("{billId}")
    public ResponseEntity<Void> deleteBill(@PathVariable("billId") int billId) {
        billService.deleteBillById(billId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Deletes an item from an existing bill.
     *
     * @param billId     The unique ID of the bill from which the item should be deleted.
     * @param menuItemId The unique ID of item to be deleted from the bill.
     * @return A ResponseEntity indicating the successful deletion with a status of "No content".
     * @throws NotFoundException           if no bill or item is found with the given ID.
     * @throws OperationForbiddenException if the bill has paid or cancelled with the given ID.
     */
    @DeleteMapping("{billId}/items/{menuItemId}")
    public ResponseEntity<Void> deleteBillItem(
            @PathVariable("billId") int billId,
            @PathVariable("menuItemId") int menuItemId
    ) {
        billService.deleteBillItem(billId, menuItemId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
