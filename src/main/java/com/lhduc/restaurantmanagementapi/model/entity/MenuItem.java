package com.lhduc.restaurantmanagementapi.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;

import static com.lhduc.restaurantmanagementapi.common.constant.DatabaseConstant.CREATED_AT_COLUMN_NAME;
import static com.lhduc.restaurantmanagementapi.common.constant.DatabaseConstant.MENU_ITEMS_TABLE_NAME;
import static com.lhduc.restaurantmanagementapi.common.constant.DatabaseConstant.UPDATED_AT_COLUMN_NAME;

@Data
@NoArgsConstructor
@Entity(name = MENU_ITEMS_TABLE_NAME)
public class MenuItem {
    private static final String MENU_ITEM_MAPPING = "menuItem";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

    @CreationTimestamp
    @Column(name = CREATED_AT_COLUMN_NAME)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = UPDATED_AT_COLUMN_NAME)
    private Timestamp updatedAt;

    @ToString.Exclude
    @OneToMany(mappedBy = MENU_ITEM_MAPPING)
    private List<BillDetail> billDetail;
}
