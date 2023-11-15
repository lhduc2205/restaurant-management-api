package com.lhduc.restaurantmanagementapi.model.entity;

import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillDetailUpdateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.validation.constraints.Min;
import lombok.Data;

import static com.lhduc.restaurantmanagementapi.common.constant.DatabaseConstant.BILL_DETAILS_TABLE_NAME;

@Data
@Entity(name = BILL_DETAILS_TABLE_NAME)
public class BillDetail {
    private static final String BILL_ID_MAPPING = "billId";
    private static final String MENU_ITEM_ID_MAPPING = "menuItemId";

    @EmbeddedId
    private BillDetailPK id;

    @Column(nullable = false)
    private Integer quantity;

    @Min(1)
    @Column(nullable = false)
    private Double pricePerUnit;

    private String description;

    @ManyToOne
    @MapsId(BILL_ID_MAPPING)
    private Bill bill;

    @ManyToOne
    @MapsId(MENU_ITEM_ID_MAPPING)
    private MenuItem menuItem;
}
