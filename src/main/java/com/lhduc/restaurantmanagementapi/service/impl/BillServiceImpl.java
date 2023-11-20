package com.lhduc.restaurantmanagementapi.service.impl;

import com.lhduc.restaurantmanagementapi.exception.OperationForbiddenException;
import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillCreateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillDetailCreateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillDetailUpdateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillFilter;
import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillUpdateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.PaginationRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.sort.SortRequest;
import com.lhduc.restaurantmanagementapi.model.dto.response.BillDto;
import com.lhduc.restaurantmanagementapi.model.entity.Bill;
import com.lhduc.restaurantmanagementapi.model.entity.BillDetail;
import com.lhduc.restaurantmanagementapi.model.entity.BillDetailPK;
import com.lhduc.restaurantmanagementapi.model.entity.MenuItem;
import com.lhduc.restaurantmanagementapi.model.mappers.BillMapper;
import com.lhduc.restaurantmanagementapi.repository.BillDetailRepository;
import com.lhduc.restaurantmanagementapi.repository.BillRepository;
import com.lhduc.restaurantmanagementapi.service.BillService;
import com.lhduc.restaurantmanagementapi.util.BillDetailRepositoryUtil;
import com.lhduc.restaurantmanagementapi.util.BillRepositoryUtil;
import com.lhduc.restaurantmanagementapi.util.MenuItemRepositoryUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;
    private final BillDetailRepository billDetailRepository;
    private final MenuItemRepositoryUtil menuItemRepositoryUtil;
    private final BillRepositoryUtil billRepositoryUtil;
    private final BillDetailRepositoryUtil billDetailRepositoryUtil;
    private final BillMapper billMapper;

    @Override
    public List<BillDto> getAllBill(BillFilter billFilter, PaginationRequest paginationRequest, SortRequest sortRequest) {
        Pageable pageRequest = PageRequest.of(paginationRequest.getOffset(), paginationRequest.getLimit(), Sort.by(sortRequest.buildSortOrders()));
        Bill probe = billMapper.convertToEntityFromFilter(billFilter);
        Page<Bill> billPage = billRepository.findAll(Example.of(probe), pageRequest);

        return billMapper.convertToDto(billPage.getContent());
    }

    @Override
    public BillDto getBillById(int billId) {
        Bill bill = billRepositoryUtil.findByIdOrThrow(billId);
        return billMapper.convertToDto(bill);
    }

    @Override
    public BillDto createBill(BillCreateRequest billRequest) {
        Bill bill = new Bill();
        List<BillDetail> billDetails = new ArrayList<>();

        for (BillDetailCreateRequest billDetailRequest : billRequest.getItems()) {
            MenuItem menuItem = menuItemRepositoryUtil.findByIdOrThrow(billDetailRequest.getMenuItemId());
            BillDetail newBillDetail = this.generateBillDetail(bill, menuItem, billDetailRequest);

            billDetails.add(newBillDetail);
        }

        bill.setBillDetails(billDetails);
        Bill createdBill = billRepository.save(bill);

        return billMapper.convertToDto(createdBill);
    }

    @Override
    public void addMoreBillItems(int billId, List<BillDetailCreateRequest> requests) {
        Bill bill = billRepositoryUtil.findByIdOrThrow(billId);
        this.validateEditableBillStatus(bill);

        List<BillDetail> billDetails = new ArrayList<>();

        for (BillDetailCreateRequest request : requests) {
            MenuItem menuItem = menuItemRepositoryUtil.findByIdOrThrow(request.getMenuItemId());
            Optional<BillDetail> billDetailOptional = billDetailRepository.findById(new BillDetailPK(bill.getId(), menuItem.getId()));

            if (billDetailOptional.isPresent()) {
                this.addToBillDetailQuantity(billDetailOptional.get(), request.getQuantity());
                billDetails.add(billDetailOptional.get());
            } else {
                BillDetail newBillDetail = generateBillDetail(bill, menuItem, request);
                billDetails.add(newBillDetail);
            }
        }

        billDetailRepository.saveAll(billDetails);
    }

    @Override
    public void updateBill(int billId, BillUpdateRequest request) {
        Bill bill = billRepositoryUtil.findByIdOrThrow(billId);
        this.validateEditableBillStatus(bill);
        bill.setPaymentStatus(request.getPaymentStatus());

        billRepository.save(bill);
    }

    @Override
    public void updateBillItems(int billId, List<BillDetailUpdateRequest> requests) {
        Bill bill = billRepositoryUtil.findByIdOrThrow(billId);
        this.validateEditableBillStatus(bill);
        List<BillDetail> billDetails = new ArrayList<>();

        for (BillDetailUpdateRequest request : requests) {
            MenuItem menuItem = menuItemRepositoryUtil.findByIdOrThrow(request.getMenuItemId());
            BillDetail billDetail = billDetailRepositoryUtil.findByIdOrThrow(new BillDetailPK(bill.getId(), menuItem.getId()));

            billDetail.setQuantity(request.getQuantity());
            billDetail.setPricePerUnit(request.getPricePerUnit());

            billDetails.add(billDetail);
        }

        billDetailRepository.saveAll(billDetails);
    }

    @Override
    public void deleteBillById(int billId) {
        Bill bill = billRepositoryUtil.findByIdOrThrow(billId);
        this.validateEditableBillStatus(bill);
        billRepository.deleteById(billId);
    }

    @Override
    public void deleteBillItem(int billId, int menuItemId) {
        BillDetail billDetail = billDetailRepositoryUtil.findByIdOrThrow(new BillDetailPK(billId, menuItemId));
        billDetailRepository.delete(billDetail);
    }

    /**
     * Generates a new {@code BillDetail} instance based on the provided {@code Bill}, {@code MenuItem},
     * and {@code BillDetailCreateRequest} objects.
     *
     * @param bill     The {@code Bill} associated with the new {@code BillDetail}.
     * @param menuItem The {@code MenuItem} associated with the new {@code BillDetail}.
     * @param request  The {@code BillDetailCreateRequest} containing information for creating the new {@code BillDetail}.
     * @return A newly created {@code BillDetail} instance.
     */
    private BillDetail generateBillDetail(Bill bill, MenuItem menuItem, BillDetailCreateRequest request) {
        BillDetail billDetailToCreate = new BillDetail();

        billDetailToCreate.setId(new BillDetailPK(bill.getId(), menuItem.getId()));
        billDetailToCreate.setQuantity(request.getQuantity());
        billDetailToCreate.setPricePerUnit(menuItem.getPrice());
        billDetailToCreate.setBill(bill);
        billDetailToCreate.setMenuItem(menuItem);

        return billDetailToCreate;
    }

    /**
     * Adds the specific quantity to an existing quantity of a {@code BillDetail} object.
     *
     * @param billDetail    The {@code BillDetail} object to update.
     * @param quantityToAdd The quantity to add to an existing quantity.
     */
    private void addToBillDetailQuantity(BillDetail billDetail, int quantityToAdd) {
        int currentQuantity = billDetail.getQuantity();
        billDetail.setQuantity(currentQuantity + quantityToAdd);
    }

    /**
     * Validates if a payment status of a {@code Bill} object allows for edit operations.
     *
     * @param bill The {@code Bill} object to validate.
     * @throws OperationForbiddenException If the payment status of the bill does not allow for edits.
     */
    private void validateEditableBillStatus(Bill bill) {
        if (!bill.getPaymentStatus().isEditable()) {
            throw new OperationForbiddenException("Unable to access the bill has been " + bill.getPaymentStatus().name().toLowerCase());
        }
    }
}
