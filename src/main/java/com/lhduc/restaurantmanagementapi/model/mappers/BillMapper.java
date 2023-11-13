package com.lhduc.restaurantmanagementapi.model.mappers;

import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillFilter;
import com.lhduc.restaurantmanagementapi.model.dto.response.BillDto;
import com.lhduc.restaurantmanagementapi.model.entity.Bill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface BillMapper extends BaseMapper<BillDto, Bill> {
    /**
     * Converts a Bill entity to Bill DTO by mapping the "billDetails" field to "details".
     *
     * @param entity The Bill entity to convert.
     * @return A BillDto representing the data from the entity with "billDetails" mapped to "details".
     */
    @Override
    @Mapping(source = "billDetails", target = "details")
    BillDto convertToDto(Bill entity);

    /**
     * Converts BillFilter object to a corresponding Bill entity.
     *
     * @param billFilter The BillFilter object to convert.
     * @return A Bill entity representing the data from BillFilter object.
     */
    Bill convertToEntityFromFilter(BillFilter billFilter);
}
