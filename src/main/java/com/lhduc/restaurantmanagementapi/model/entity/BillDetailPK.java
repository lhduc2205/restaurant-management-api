package com.lhduc.restaurantmanagementapi.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class BillDetailPK implements Serializable {
    @Column(name = "bill_id")
    private Integer billId;

    @Column(name = "menu_item_id")
    private Integer menuItemId;
}
