package com.lhduc.restaurantmanagementapi.util;

import com.lhduc.restaurantmanagementapi.exception.NotFoundException;
import com.lhduc.restaurantmanagementapi.model.entity.BillDetail;
import com.lhduc.restaurantmanagementapi.model.entity.BillDetailPK;
import com.lhduc.restaurantmanagementapi.repository.BillDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.lhduc.restaurantmanagementapi.common.constant.MessageConstant.BILL_DETAIL_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class BillDetailRepositoryUtil {
    private final BillDetailRepository repository;

    /**
     * Finds a {@code BillDetail} entity by its composite primary key ({@code BillDetailPK}) or
     * throws a {@code NotFoundException} if the entity is not found.
     *
     * @param id The composite primary key of the {@code BillDetail} entity to be found.
     * @return The found {@code BillDetail} entity.
     * @throws NotFoundException If the {@code BillDetail} entity with the given composite primary key is not found.
     */
    public BillDetail findByIdOrThrow(BillDetailPK id) {
        return RepositoryUtil.findEntityByIdOrThrow(id, repository, BILL_DETAIL_NOT_FOUND);
    }
}
