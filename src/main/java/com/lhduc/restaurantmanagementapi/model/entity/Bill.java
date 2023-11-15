package com.lhduc.restaurantmanagementapi.model.entity;

import com.lhduc.restaurantmanagementapi.common.enums.PaymentStatus;
import com.lhduc.restaurantmanagementapi.model.dto.request.bill.BillUpdateRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;

import static com.lhduc.restaurantmanagementapi.common.constant.DatabaseConstant.BILLS_TABLE_NAME;
import static com.lhduc.restaurantmanagementapi.common.constant.DatabaseConstant.CREATED_AT_COLUMN_NAME;
import static com.lhduc.restaurantmanagementapi.common.constant.DatabaseConstant.UPDATED_AT_COLUMN_NAME;

@Data
@Entity(name = BILLS_TABLE_NAME)
public class Bill {
    private static final String PAYMENT_STATUS_COLUMN_NAME = "payment_status";
    private static final String BILL_MAPPING = "bill";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = PAYMENT_STATUS_COLUMN_NAME, nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.UNPAID;

    @CreationTimestamp
    @Column(name = CREATED_AT_COLUMN_NAME)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = UPDATED_AT_COLUMN_NAME)
    private Timestamp updatedAt;

    @OneToMany(mappedBy = BILL_MAPPING, cascade = CascadeType.ALL)
    private List<BillDetail> billDetails;
}
