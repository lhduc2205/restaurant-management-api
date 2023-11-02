package com.lhduc.restaurantmanagementapi.model.mappers;

import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillFilter;
import com.lhduc.restaurantmanagementapi.model.dto.response.BillDto;
import com.lhduc.restaurantmanagementapi.model.entity.Bill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface BillMapper extends BaseMapper<BillDto, Bill> {
    @Override
    @Mapping(source = "billDetails", target = "details")
    BillDto convertToDto(Bill entity);

    Bill convertToEntityFromFilter(BillFilter billFilter);
}
