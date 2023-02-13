package com.procurement.apps.model;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class ProcurementDetailResponse {
    private UUID id;
    private ProductResponse product_detail;
    private String product_id;
    private int quantity;
    private float price;
    private float subtotal;
    private String priority;
    private String notes;
    private String user_id;
    private Boolean is_deleted;
    private Date created_at;
    private Date updated_at;
    private String created_by;
    private String updated_by;
}


