package com.lhduc.restaurantmanagementapi.service.impl;

import com.lhduc.restaurantmanagementapi.exception.NotFoundException;
import com.lhduc.restaurantmanagementapi.exception.OperationForbiddenException;
import com.lhduc.restaurantmanagementapi.model.dto.request.PaginationRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillCreateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillDetailCreateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillDetailUpdateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillFilter;
import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillUpdateRequest;
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
import com.lhduc.restaurantmanagementapi.util.RepositoryUtil;
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

import static com.lhduc.restaurantmanagementapi.common.constant.MessageConstant.BILL_DETAIL_NOT_FOUND;
import static com.lhduc.restaurantmanagementapi.common.constant.MessageConstant.BILL_NOT_FOUND;
import static com.lhduc.restaurantmanagementapi.common.constant.MessageConstant.MENU_ITEM_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;
    private final BillDetailRepository billDetailRepository;
    private final MenuItemRepository menuItemRepository;
    private final BillMapper billMapper;

    /**
     * Retrieves a paginated list of bills based on the provided filters, pagination, and sorting criteria.
     *
     * @param billFilter        The filter criteria for bills.
     * @param paginationRequest The pagination details, including offset and limit.
     * @param sortRequest       The sorting criteria for the results.
     * @return A list of {@link BillDto} representing the paginated bills.
     */
    @Override
    public List<BillDto> getAllBill(BillFilter billFilter, PaginationRequest paginationRequest, SortRequest sortRequest) {
        Pageable pageRequest = PageRequest.of(paginationRequest.getOffset(), paginationRequest.getLimit(), Sort.by(sortRequest.buildSortOrders()));
        Bill probe = billMapper.convertToEntityFromFilter(billFilter);
        Page<Bill> billPage = billRepository.findAll(Example.of(probe), pageRequest);

        return billMapper.convertToDto(billPage.getContent());
    }

    /**
     * Retrieves a bill by its unique identifier.
     *
     * @param billId The unique identifier of the bill.
     * @return The {@link BillDto} representing the retrieved bill.
     * @throws NotFoundException if the bill with the given ID is not found.
     */
    @Override
    public BillDto getBillById(int billId) {
        Bill bill = this.findBillByIdOrThrow(billId);
        return billMapper.convertToDto(bill);
    }

    /**
     * Creates a new bill based on the provided creation request.
     *
     * @param billRequest The request containing information to create a new bill.
     * @return The {@link BillDto} representing the newly created bill.
     */
    @Override
    public BillDto createBill(BillCreateRequest billRequest) {
        Bill bill = new Bill();
        List<BillDetail> billDetails = new ArrayList<>();

        for (BillDetailCreateRequest billDetailRequest : billRequest.getItems()) {
            MenuItem menuItem = this.findMenuItemByIdOrThrow(billDetailRequest.getMenuItemId());
            BillDetail newBillDetail = this.generateBillDetail(bill, menuItem, billDetailRequest);

            billDetails.add(newBillDetail);
        }

        bill.setBillDetails(billDetails);
        Bill createdBill = billRepository.save(bill);

        return billMapper.convertToDto(createdBill);
    }

    /**
     * Adds more bill items to an existing bill.
     *
     * @param billId   The unique identifier of the bill to which items will be added.
     * @param requests A list of {@link BillDetailCreateRequest} representing the bill items to be added.
     */
    @Override
    public void addMoreBillItems(int billId, List<BillDetailCreateRequest> requests) {
        Bill bill = this.findBillByIdOrThrow(billId);
        this.validateEditableBillStatus(bill);

        List<BillDetail> billDetails = new ArrayList<>();

        for (BillDetailCreateRequest request : requests) {
            MenuItem menuItem = this.findMenuItemByIdOrThrow(request.getMenuItemId());
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

    /**
     * Updates the payment status of an existing bill.
     *
     * @param billId  The unique identifier of the bill to be updated.
     * @param request The request containing the updated payment status.
     */
    @Override
    public void updateBill(int billId, BillUpdateRequest request) {
        Bill bill = this.findBillByIdOrThrow(billId);
        this.validateEditableBillStatus(bill);
        bill.setPaymentStatus(request.getPaymentStatus());

        billRepository.save(bill);
    }

    /**
     * Updates bill items of an existing bill.
     *
     * @param billId   The unique identifier of the bill whose items will be updated.
     * @param requests A list of {@link BillDetailUpdateRequest} representing the updated bill items.
     */
    @Override
    public void updateBillItems(int billId, List<BillDetailUpdateRequest> requests) {
        Bill bill = this.findBillByIdOrThrow(billId);
        this.validateEditableBillStatus(bill);
        List<BillDetail> billDetails = new ArrayList<>();

        for (BillDetailUpdateRequest request : requests) {
            MenuItem menuItem = this.findMenuItemByIdOrThrow(request.getMenuItemId());
            BillDetail billDetail = this.findBillDetailByIdOrThrow(new BillDetailPK(bill.getId(), menuItem.getId()));

            billDetail.setQuantity(request.getQuantity());
            billDetail.setPricePerUnit(request.getPricePerUnit());

            billDetails.add(billDetail);
        }

        billDetailRepository.saveAll(billDetails);
    }

    /**
     * Deletes a bill by its unique identifier.
     *
     * @param billId The unique identifier of the bill to be deleted.
     * @throws OperationForbiddenException if the bill is not in an editable status.
     * @throws NotFoundException   if the bill with the given ID is not found.
     */
    @Override
    public void deleteBillById(int billId) {
        Bill bill = this.findBillByIdOrThrow(billId);
        this.validateEditableBillStatus(bill);
        billRepository.deleteById(billId);
    }

    /**
     * Deletes a bill item from an existing bill.
     *
     * @param billId     The unique identifier of the bill from which the item will be deleted.
     * @param menuItemId The unique identifier of the menu item to be deleted from the bill.
     * @throws OperationForbiddenException if the bill item cannot be deleted.
     */
    @Override
    public void deleteBillItem(int billId, int menuItemId) {
        BillDetail billDetail = this.findBillDetailByIdOrThrow(new BillDetailPK(billId, menuItemId));
        this.validateEditableBillStatus(billDetail.getBill());
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

    /**
     * Finds a {@code Bill} entity by its ID or throws a {@code NotFoundException}
     * if the entity is not found.
     *
     * @param billId The ID of the {@code Bill} entity to be found.
     * @return The found {@code Bill} entity.
     * @throws NotFoundException If the {@code Bill} entity with the given ID is not found.
     */
    private Bill findBillByIdOrThrow(int billId) {
        return RepositoryUtil.findEntityByIdOrThrow(billId, billRepository, BILL_NOT_FOUND);
    }

    /**
     * Finds a {@code BillDetail} entity by its composite primary key ({@code BillDetailPK}) or
     * throws a {@code NotFoundException} if the entity is not found.
     *
     * @param id The composite primary key of the {@code BillDetail} entity to be found.
     * @return The found {@code BillDetail} entity.
     * @throws NotFoundException If the {@code BillDetail} entity with the given composite primary key is not found.
     */
    private BillDetail findBillDetailByIdOrThrow(BillDetailPK id) {
        return RepositoryUtil.findEntityByIdOrThrow(id, billDetailRepository, BILL_DETAIL_NOT_FOUND);
    }

    /**
     * Finds a {@code MenuItem} entity by its ID or throws a {@code NotFoundException}
     * if the entity is not found.
     *
     * @param menuItemId The ID of the {@code MenuItem} entity to be found.
     * @return The found {@code MenuItem} entity.
     * @throws NotFoundException If the {@code MenuItem} entity with the given ID is not found.
     */
    private MenuItem findMenuItemByIdOrThrow(int menuItemId) {
        return RepositoryUtil.findEntityByIdOrThrow(menuItemId, menuItemRepository, MENU_ITEM_NOT_FOUND);
    }
}
