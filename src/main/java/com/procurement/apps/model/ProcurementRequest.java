package com.procurement.apps.model;

import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;
import java.util.List;

@Data
public class ProcurementRequest {
    private String id;
    @NotBlank(message = "name must be not blank")
    private String name;
    @NotBlank(message = "user_id must be not blank")
    private String user_id;
    private Boolean is_approved_manager = false;
    private Boolean is_approved_finance = false;
    private Boolean is_rejected_manager = false;
    private Boolean is_rejected_finance = false;
    private Boolean is_deleted = false;
    @Valid
    private List<ProcurementDetailRequest> detail_procurement;
}
