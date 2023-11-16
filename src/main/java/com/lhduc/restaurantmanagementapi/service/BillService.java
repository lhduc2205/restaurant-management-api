package com.lhduc.restaurantmanagementapi.service;

import com.lhduc.restaurantmanagementapi.exception.NotFoundException;
import com.lhduc.restaurantmanagementapi.exception.OperationForbiddenException;
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
    /**
     * Retrieves a list of bills based on filtering, pagination and sorting criteria.
     *
     * @param billFilter        An object specifying the filter criteria for bills.
     * @param paginationRequest An object specifying the pagination parameters.
     * @param sortRequest       An object specifying the sorting criteria.
     * @return A list of BillDto objects that match the provided criteria.
     */
    List<BillDto> getAllBill(BillFilter billFilter, PaginationRequest paginationRequest, SortRequest sortRequest);

    /**
     * Retrieves a bill by its unique ID.
     *
     * @param billId The unique ID of the bill to be retrieved.
     * @return A BillDto representing the retrieved bill.
     * @throws NotFoundException if no bill is found with the given ID.
     */
    BillDto getBillById(int billId);

    /**
     * Creates new bill based on the provided request, which includes a list of items and their quantities.
     *
     * @param billRequest The request object containing the details for creating the bill,
     *                    include items and quantities.
     * @return A BillDto representing the created bill.
     */
    BillDto createBill(BillCreateRequest billRequest);

    /**
     * Adds more items to an existing bill.
     *
     * @param billId             The unique ID of the bill to which items should be added.
     * @param billDetailsRequest The request object containing the details of the items to add to the bill.
     * @throws NotFoundException           if no bill or item is found with the given ID.
     * @throws OperationForbiddenException if the bill has paid or cancelled with the given ID.
     */
    void addMoreBillItems(int billId, List<BillDetailCreateRequest> billDetailsRequest);


    /**
     * Updates the status of an existing bill.
     *
     * @param billId      The unique ID of the bill to be updated.
     * @param billRequest The request object containing the details for updating the bill,
     *                    including item or status of the bill.
     * @throws NotFoundException           if no bill or item is found with the given ID.
     * @throws OperationForbiddenException if the bill has paid or cancelled with the given ID.
     */
    void updateBill(int billId, BillUpdateRequest billRequest);

    /**
     * Updates items of an existing bill.
     *
     * @param billId             The unique ID of the bill to be updated.
     * @param billDetailsRequest The request object containing the details for updating the bill.
     * @throws NotFoundException           if no bill or item is found with the given ID.
     * @throws OperationForbiddenException if the bill has paid or cancelled with the given ID.
     */
    void updateBillItems(int billId, List<BillDetailUpdateRequest> billDetailsRequest);

    /**
     * Deletes an existing bill.
     *
     * @param billId The unique ID of the bill to be deleted.
     * @throws NotFoundException           if no bill or item is found with the given ID.
     * @throws OperationForbiddenException if the bill has paid or cancelled with the given ID.
     */
    void deleteBillById(int billId);

    /**
     * Deletes an item from an existing bill.
     *
     * @param billId     The unique ID of the bill from which the item should be deleted.
     * @param menuItemId The unique ID of item to be deleted from the bill.
     * @throws NotFoundException           if no bill or item is found with the given ID.
     * @throws OperationForbiddenException if the bill has paid or cancelled with the given ID.
     */
    void deleteBillItem(int billId, int menuItemId);
}
