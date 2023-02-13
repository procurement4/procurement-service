package com.procurement.apps.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "ProcurementDetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcurementDetail {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id")
    private UUID id;
    @Column(name = "procurement_id")
    private UUID procurement_id;
    @Column(name = "product_id")
    private String product_id;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "price")
    private float price;
    @Column(name = "subtotal")
    private float subtotal;
    @Column(name = "priority")
    private String priority;
    @Column(name = "notes")
    private String notes;
    @Column(name = "user_id")
    private String user_id;
    @Column(name = "is_deleted")
    private Boolean is_deleted;
    @Column(name = "created_at")
    private Date created_at;
    @Column(name = "updated_at")
    private Date updated_at;
    @Column(name = "created_by")
    private String created_by;
    @Column(name = "updated_by")
    private String updated_by;
}
