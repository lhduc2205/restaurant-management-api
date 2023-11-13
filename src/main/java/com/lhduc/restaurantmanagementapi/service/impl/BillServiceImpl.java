package com.lhduc.restaurantmanagementapi.service.impl;

import com.lhduc.restaurantmanagementapi.common.constant.MessageConstant;
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
import com.lhduc.restaurantmanagementapi.model.entity.Bill;
import com.lhduc.restaurantmanagementapi.model.entity.BillDetail;
import com.lhduc.restaurantmanagementapi.model.entity.BillDetailPK;
import com.lhduc.restaurantmanagementapi.model.entity.MenuItem;
import com.lhduc.restaurantmanagementapi.model.mappers.BillMapper;
import com.lhduc.restaurantmanagementapi.repository.BillDetailRepository;
import com.lhduc.restaurantmanagementapi.repository.BillRepository;
import com.lhduc.restaurantmanagementapi.repository.MenuItemRepository;
import com.lhduc.restaurantmanagementapi.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;
    private final BillDetailRepository billDetailRepository;
    private final MenuItemRepository menuItemRepository;
    private final BillMapper billMapper;

    @Override
    public List<BillDto> getAllBill(BillFilter billFilter, PaginationRequest paginationRequest, SortRequest sortRequest) {
        Pageable pageRequest = PageRequest.of(paginationRequest.getOffset(), paginationRequest.getLimit(), Sort.by(sortRequest.buildSortOrders()));
        Bill probe = billMapper.convertToEntityFromFilter(billFilter);
        Page<Bill> billPage = billRepository.findAll(Example.of(probe), pageRequest);
        return billMapper.convertToDto(billPage.getContent());
    }

    @Override
    public BillDto getBillById(int id) {
        Bill bill = this.getExistedBill(id);
        return billMapper.convertToDto(bill);
    }

    @Override
    public BillDto createBill(BillCreateRequest billRequest) {
        Bill bill = new Bill();
        List<BillDetail> billDetailsToCreate = this.createBillDetailsForBill(bill, billRequest.getItems());
        bill.setBillDetails(billDetailsToCreate);
        Bill createdBill = billRepository.save(bill);
        return billMapper.convertToDto(createdBill);
    }

    @Override
    public void addMoreBillItems(int billId, List<BillDetailCreateRequest> billDetailsRequest) {
        Bill bill = this.getExistedBill(billId);
        this.ensureBillIsEditable(bill);
        this.handleCreateOrUpdateBillDetailsFromRequest(bill, billDetailsRequest);
    }

    @Override
    public void updateBill(int billId, BillUpdateRequest billUpdateRequest) {
        Bill bill = getExistedBill(billId);
        this.ensureBillIsEditable(bill);
        bill.update(billUpdateRequest);
        for (BillDetailUpdateRequest billDetailUpdateRequest : billUpdateRequest.getDetails()) {
            MenuItem menuItem = this.getExistedMenuItem(billDetailUpdateRequest.getMenuItemId());
            BillDetail billDetailToUpdate = this.getExistedBillDetail(bill.getId(), menuItem.getId());
            billDetailToUpdate.update(billDetailUpdateRequest);
        }
        billRepository.save(bill);
    }

    @Override
    public void updateBillItems(int billId, List<BillDetailUpdateRequest> billDetailsRequest) {
        Bill bill = this.getExistedBill(billId);
        this.ensureBillIsEditable(bill);
        List<BillDetail> billDetailsToUpdate = new ArrayList<>();
        for (BillDetailUpdateRequest billDetailUpdateRequest : billDetailsRequest) {
            MenuItem menuItem = this.getExistedMenuItem(billDetailUpdateRequest.getMenuItemId());
            BillDetail billDetailToUpdate = this.getExistedBillDetail(bill.getId(), menuItem.getId());
            billDetailToUpdate.update(billDetailUpdateRequest);
            billDetailsToUpdate.add(billDetailToUpdate);
        }
        billDetailRepository.saveAll(billDetailsToUpdate);
    }

    @Override
    public void deleteBillById(int id) {
        Bill bill = getExistedBill(id);
        this.ensureBillIsEditable(bill);
        billRepository.deleteById(id);
    }

    @Override
    public void deleteBillItem(int billId, int menuItemId) {
        BillDetail billDetail = getExistedBillDetail(billId, menuItemId);
        billDetailRepository.delete(billDetail);
    }

    private void handleCreateOrUpdateBillDetailsFromRequest(Bill bill, List<BillDetailCreateRequest> request) {
        List<BillDetail> billDetailsToSave = new ArrayList<>();
        for (BillDetailCreateRequest billDetailRequest : request) {
            MenuItem menuItem = this.getExistedMenuItem(billDetailRequest.getMenuItemId());
            if (existsBillDetail(bill.getId(), menuItem.getId())) {
                BillDetail existedBillDetail = this.getExistedBillDetail(bill.getId(), menuItem.getId());
                this.addToBillDetailQuantity(existedBillDetail, billDetailRequest.getQuantity());
                billDetailsToSave.add(existedBillDetail);
            } else {
                BillDetail newBillDetail = createBillDetail(bill, menuItem, billDetailRequest);
                billDetailsToSave.add(newBillDetail);
            }
        }

        billDetailRepository.saveAll(billDetailsToSave);
    }

    private List<BillDetail> createBillDetailsForBill(Bill bill, List<BillDetailCreateRequest> request) {
        List<BillDetail> billDetailsToCreate = new ArrayList<>();
        for (BillDetailCreateRequest billDetailRequest : request) {
            MenuItem menuItem = this.getExistedMenuItem(billDetailRequest.getMenuItemId());
            BillDetail newBillDetail = this.createBillDetail(bill, menuItem, billDetailRequest);
            billDetailsToCreate.add(newBillDetail);
        }
        return billDetailsToCreate;
    }

    private BillDetail createBillDetail(Bill bill, MenuItem menuItem, BillDetailCreateRequest billDetailInfo) {
        BillDetail billDetailToCreate = new BillDetail();
        billDetailToCreate.setId(new BillDetailPK(bill.getId(), menuItem.getId()));
        billDetailToCreate.setQuantity(billDetailInfo.getQuantity());
        billDetailToCreate.setPricePerUnit(menuItem.getPrice());
        billDetailToCreate.setDescription(billDetailInfo.getDescription());
        billDetailToCreate.setBill(bill);
        billDetailToCreate.setMenuItem(menuItem);
        return billDetailToCreate;
    }

    private void addToBillDetailQuantity(BillDetail billDetail, int quantityToAdd) {
        int currentQuantity = billDetail.getQuantity();
        billDetail.setQuantity(currentQuantity + quantityToAdd);
    }

    private boolean existsBillDetail(int billId, int menuItemId) {
        return billDetailRepository.existsById(new BillDetailPK(billId, menuItemId));
    }

    private Bill getExistedBill(int id) {
        return billRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(MessageConstant.BILL_NOT_FOUND));
    }

    private BillDetail getExistedBillDetail(int billId, int menuItemId) {
        return billDetailRepository
                .findById(new BillDetailPK(billId, menuItemId))
                .orElseThrow(() -> new NotFoundException(MessageConstant.BILL_DETAIL_NOT_FOUND));
    }

    private MenuItem getExistedMenuItem(int menuItemId) {
        return menuItemRepository
                .findById(menuItemId)
                .orElseThrow(() -> new NotFoundException(MessageConstant.MENU_ITEM_NOT_FOUND));
    }

    private void ensureBillIsEditable(Bill bill) {
        if (!bill.getPaymentStatus().isEditable()) {
            throw new OperationForbiddenException("Unable to access the bill has been " + bill.getPaymentStatus().name().toLowerCase());
        }
    }
}
