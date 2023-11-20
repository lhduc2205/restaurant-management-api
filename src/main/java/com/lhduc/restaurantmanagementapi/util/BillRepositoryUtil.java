package com.lhduc.restaurantmanagementapi.util;

import com.lhduc.restaurantmanagementapi.exception.NotFoundException;
import com.lhduc.restaurantmanagementapi.model.entity.Bill;
import com.lhduc.restaurantmanagementapi.repository.BillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.lhduc.restaurantmanagementapi.common.constant.MessageConstant.BILL_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class BillRepositoryUtil {
    private final BillRepository repository;

    /**
     * Finds a {@code Bill} entity by its ID or throws a {@code NotFoundException}
     * if the entity is not found.
     *
     * @param billId The ID of the {@code Bill} entity to be found.
     * @return The found {@code Bill} entity.
     * @throws NotFoundException If the {@code Bill} entity with the given ID is not found.
     */
    public Bill findByIdOrThrow(int billId) {
        return RepositoryUtil.findEntityByIdOrThrow(billId, repository, BILL_NOT_FOUND);
    }
}
