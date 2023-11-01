package com.lhduc.restaurantmanagementapi.model.mappers;

import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillCreateRequest;
import com.lhduc.restaurantmanagementapi.model.dto.response.BillDetailDto;
import com.lhduc.restaurantmanagementapi.model.dto.response.BillDto;
import com.lhduc.restaurantmanagementapi.model.entity.Bill;
import com.lhduc.restaurantmanagementapi.model.entity.BillDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface BillMapper extends BaseMapper<BillDto, Bill> {
    @Mapping(target = "paymentStatus", ignore = true)
    @Mapping(source = "details", target = "billDetails")
    Bill convertToEntityFromRequest(BillCreateRequest requestDto);

    @Override
    @Mapping(source = "billDetails", target = "details")
    BillDto convertToDto(Bill entity);
}
