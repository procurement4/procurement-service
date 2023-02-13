package com.procurement.apps.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "Procurements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Procurement {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id")
    private UUID id;
    @Column(name = "name")
    private String name;
    @Column(name = "is_approved_manager")
    private Boolean is_approved_manager;
    @Column(name = "is_approved_finance")
    private Boolean is_approved_finance;
    @Column(name = "is_rejected_manager")
    private Boolean is_rejected_manager;
    @Column(name = "is_rejected_finance")
    private Boolean is_rejected_finance;
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
