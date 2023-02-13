package com.procurement.apps.model;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateStockRequest {
    private String id;
    private int stock;
    private String user_id;
}
