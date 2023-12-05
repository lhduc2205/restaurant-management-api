package com.lhduc.restaurantmanagementapi.service.impl;

import com.lhduc.restaurantmanagementapi.common.enums.PaymentStatus;
import com.lhduc.restaurantmanagementapi.exception.NotFoundException;
import com.lhduc.restaurantmanagementapi.exception.OperationForbiddenException;
import com.lhduc.restaurantmanagementapi.model.dto.request.PaginationRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillCreateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillDetailCreateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillFilter;
import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillUpdateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.sort.BillSortRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.sort.SortRequest;
import com.lhduc.restaurantmanagementapi.model.dto.response.BillDto;
import com.lhduc.restaurantmanagementapi.model.entity.Bill;
import com.lhduc.restaurantmanagementapi.model.entity.BillDetailPK;
import com.lhduc.restaurantmanagementapi.model.entity.MenuItem;
import com.lhduc.restaurantmanagementapi.repository.BillDetailRepository;
import com.lhduc.restaurantmanagementapi.repository.BillRepository;
import com.lhduc.restaurantmanagementapi.repository.MenuItemRepository;
import com.lhduc.restaurantmanagementapi.service.BillService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BillServiceImplTest {
    @Autowired
    private BillServiceImpl billService;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillDetailRepository billDetailRepository;

    /**
     * Setup method executed before all tests, creating two menu items.
     */
    @BeforeAll
    public void setup() {
        MenuItem menuItem1 = new MenuItem();
        menuItem1.setName("Bun bo");
        menuItem1.setPrice(20000d);

        MenuItem menuItem2 = new MenuItem();
        menuItem2.setName("Com tam");
        menuItem2.setPrice(30000d);

        menuItemRepository.saveAll(Arrays.asList(menuItem1, menuItem2));
    }

    /**
     * Test case to create a bill with two items and verify the details and payment status.
     *
     * @see BillService#createBill(BillCreateRequest)
     */
    @Test
    @Order(1)
    void testCreateBillWithTwoItems() {
        BillCreateRequest billCreateRequest = new BillCreateRequest();
        List<BillDetailCreateRequest> items = new ArrayList<>();

        for (int i = 1; i <= 2; i++) {
            BillDetailCreateRequest billDetailCreateRequest = new BillDetailCreateRequest();
            billDetailCreateRequest.setMenuItemId(i);
            billDetailCreateRequest.setQuantity(10);
            items.add(billDetailCreateRequest);
        }

        billCreateRequest.setItems(items);

        BillDto bill = billService.createBill(billCreateRequest);
        assertNotNull(bill);
        assertEquals(PaymentStatus.UNPAID, bill.getPaymentStatus());
        assertEquals(2, bill.getDetails().size());
    }

    /**
     * Test case to create an empty bill and verify the default payment status.
     *
     * @see BillService#createBill(BillCreateRequest)
     */
    @Test
    @Order(2)
    void testCreateEmptyBill() {
        BillCreateRequest billCreateRequest = new BillCreateRequest();
        BillDto bill = billService.createBill(billCreateRequest);
        assertNotNull(bill);
        assertEquals(PaymentStatus.UNPAID, bill.getPaymentStatus());
        assertEquals(0, bill.getDetails().size());
    }

    /**
     * Test case to retrieve all bills and ensure there are two bills.
     *
     * @see BillService#getAllBill(BillFilter, PaginationRequest, SortRequest)
     */
    @Test
    @Order(3)
    void testGetAllBill() {
        List<BillDto> bills = billService.getAllBill(new BillFilter(), new PaginationRequest(), new SortRequest());
        assertEquals(2, bills.size());
    }

    /**
     * Test case to retrieve bills with filtering by payment status and verify the expected results.
     *
     * @see BillService#getAllBill(BillFilter, PaginationRequest, SortRequest)
     */
    @Test
    @Order(4)
    void testGetAllBillWithFiltering() {
        BillFilter billFilter = new BillFilter();

        // Find all bill has UNPAID status
        billFilter.setPaymentStatus(PaymentStatus.UNPAID);
        List<BillDto> bills = billService.getAllBill(billFilter, new PaginationRequest(), new SortRequest());
        assertEquals(2, bills.size());

        // Find all bill has PAID status
        billFilter.setPaymentStatus(PaymentStatus.PAID);
        bills = billService.getAllBill(billFilter, new PaginationRequest(), new SortRequest());
        assertEquals(0, bills.size());
    }

    /**
     * Test case to retrieve bills with descending sorting by ID and ensure proper sorting.
     *
     * @see BillService#getAllBill(BillFilter, PaginationRequest, SortRequest)
     */
    @Test
    @Order(5)
    void testGetAllBillWithDescSorting() {
        BillSortRequest sortRequest = new BillSortRequest();
        sortRequest.setSort("-id");

        // Sorts id by descending
        List<BillDto> bills = billService.getAllBill(new BillFilter(), new PaginationRequest(), sortRequest);
        assertEquals(2, bills.size());
        assertEquals(2, bills.get(0).getId());
        assertEquals(1, bills.get(1).getId());
    }

    /**
     * Test case to retrieve bills with pagination and verify the expected number of items per page.
     *
     * @see BillService#getAllBill(BillFilter, PaginationRequest, SortRequest)
     */
    @Test
    @Order(6)
    void testGetAllBillWithPaginating() {
        PaginationRequest paginationRequest = new PaginationRequest();

        // Paginates with Offset = 0 & Limit = 1
        paginationRequest.setLimit(1);
        List<BillDto> bills = billService.getAllBill(new BillFilter(), paginationRequest, new SortRequest());
        assertEquals(1, bills.size());
        assertEquals(1, bills.get(0).getId());

        // Paginates with Offset = 0 & Limit = 2
        paginationRequest.setLimit(2);
        bills = billService.getAllBill(new BillFilter(), paginationRequest, new SortRequest());
        assertEquals(2, bills.size());
        assertEquals(1, bills.get(0).getId());
        assertEquals(2, bills.get(1).getId());

        // Paginates with Offset = 1 & Limit = 1
        paginationRequest.setOffset(1);
        paginationRequest.setLimit(1);
        bills = billService.getAllBill(new BillFilter(), paginationRequest, new SortRequest());
        assertEquals(1, bills.size());
        assertEquals(2, bills.get(0).getId());
    }

    /**
     * Test case to retrieve a specific bill by its ID and ensure it is found.
     *
     * @see BillService#getBillById(int)
     */
    @Test
    @Order(7)
    void testGetBillById() {
        int billId = 1;
        BillDto bill = billService.getBillById(billId);

        assertNotNull(bill);
        assertEquals(billId, bill.getId());
    }

    /**
     * Test case to attempt to retrieve a non-existent bill by its ID, expecting a NotFoundException.
     *
     * @see BillService#getBillById(int)
     */
    @Test
    @Order(8)
    void testGetBillById_throwNotFoundException() {
        int notFoundId = 999;
        assertThrows(NotFoundException.class, () -> billService.getBillById(notFoundId));
    }

    /**
     * Test case to add more bill items to an existing bill and verify their addition.
     *
     * @see BillService#addMoreBillItems(int, List)
     */
    @Test
    @Order(9)
    void testAddMoreBillItems() {
        int billId = 2;
        int menuItemId = 1;
        int menuItemQuantity = 10;

        Bill bill = billRepository.findById(billId).orElseThrow();
        assertNotNull(bill);
        assertEquals(0, bill.getBillDetails().size());

        List<BillDetailCreateRequest> requests = this.prepareBillDetailCreateRequests(menuItemId, menuItemQuantity);

        billService.addMoreBillItems(billId, requests);

        assertTrue(billDetailRepository.existsById(new BillDetailPK(billId, menuItemId)));
    }

    /**
     * Prepares a list of BillDetailCreateRequest with the given menuItemId and quantity.
     *
     * @param menuItemId The ID of the menu item for the BillDetailCreateRequest.
     * @param quantity   The quantity of the menu item for the BillDetailCreateRequest.
     * @return A list containing a single BillDetailCreateRequest with the specified menuItemId and quantity.
     */
    List<BillDetailCreateRequest> prepareBillDetailCreateRequests(int menuItemId, int quantity) {
        List<BillDetailCreateRequest> billDetailCreateRequests = new ArrayList<>();

        BillDetailCreateRequest detailRequest = new BillDetailCreateRequest();
        detailRequest.setMenuItemId(menuItemId);
        detailRequest.setQuantity(quantity);
        billDetailCreateRequests.add(detailRequest);

        return billDetailCreateRequests;
    }

    /**
     * Test case to attempt to add more bill items to non-existent bills, expecting NotFoundException.
     *
     * @see BillService#addMoreBillItems(int, List)
     */
    @Test
    @Order(9)
    void testAddMoreBillItems_throwNotFoundException() {
        // Bill not found
        int billNotFoundId = 999;
        int menuItemId = 1;
        List<BillDetailCreateRequest> request1 = this.prepareBillDetailCreateRequests(menuItemId, 10);
        assertThrows(NotFoundException.class, () -> billService.addMoreBillItems(billNotFoundId, request1));

        // Menu item not found
        int billId = 2;
        int menuItemNotFoundId = 999;
        List<BillDetailCreateRequest> request2 = this.prepareBillDetailCreateRequests(menuItemNotFoundId, 10);
        assertThrows(NotFoundException.class, () -> billService.addMoreBillItems(billId, request2));

    }

    /**
     * Test case to update the payment status of a bill and verify the changes.
     *
     * @see BillService#updateBill(int, BillUpdateRequest)
     */
    @Test
    @Order(10)
    void updateBill() {
        int billId = 1;
        PaymentStatus statusToUpdate = PaymentStatus.PAID;

        BillDto bill = billService.getBillById(billId);
        assertEquals(PaymentStatus.UNPAID, bill.getPaymentStatus());

        BillUpdateRequest billUpdateRequest = new BillUpdateRequest();
        billUpdateRequest.setPaymentStatus(statusToUpdate);

        billService.updateBill(billId, billUpdateRequest);

        BillDto billAfterUpdate = billService.getBillById(billId);
        assertEquals(statusToUpdate, billAfterUpdate.getPaymentStatus());
    }

    /**
     * Test case to attempt to update the payment status of a bill to an invalid status, expecting OperationForbiddenException.
     *
     * @see BillService#updateBill(int, BillUpdateRequest)
     */
    @Test
    @Order(11)
    void updateBill_throwException() {
        int billId = 1;
        PaymentStatus statusToUpdate = PaymentStatus.CANCELLED;

        BillDto bill = billService.getBillById(billId);
        assertEquals(PaymentStatus.PAID, bill.getPaymentStatus());

        BillUpdateRequest billUpdateRequest = new BillUpdateRequest();
        billUpdateRequest.setPaymentStatus(statusToUpdate);

        assertThrows(OperationForbiddenException.class, () -> billService.updateBill(billId, billUpdateRequest));
    }
}