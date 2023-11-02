package com.lhduc.restaurantmanagementapi.model.entity;

import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillDetailUpdateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "bill_details")
public class BillDetail {
    @EmbeddedId
    private BillDetailPK id;

    @Column(nullable = false)
    private Integer quantity;

    @Min(1)
    @Column(nullable = false)
    private Double pricePerUnit;

    private String description;

    @ManyToOne
    @MapsId("billId")
    private Bill bill;

    @ManyToOne
    @MapsId("menuItemId")
    private MenuItem menuItem;

    public void update(BillDetailUpdateRequest billDetailUpdateRequest) {
        if (billDetailUpdateRequest.getQuantity() != 0) {
            this.quantity = billDetailUpdateRequest.getQuantity();
        }
        if (billDetailUpdateRequest.getPricePerUnit() != 0) {
            this.pricePerUnit = billDetailUpdateRequest.getPricePerUnit();
        }
        this.description = billDetailUpdateRequest.getDescription();
    }
}
