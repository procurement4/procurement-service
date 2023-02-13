package com.procurement.apps.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class ProcurementDetailRequest {
    @NotBlank(message = "product_id must be not blank")
    private String product_id;
    @Min(value = 1, message = "quantity minimum 1")
    private int quantity;
    @Min(value = 1, message = "price minimum 1")
    private float price;
    @NotBlank(message = "priority must be not blank")
    private String priority;
    private String notes;
    private Boolean is_deleted = false;
}
