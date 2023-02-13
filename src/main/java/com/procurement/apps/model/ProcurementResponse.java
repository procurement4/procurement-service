package com.procurement.apps.model;

import jakarta.persistence.Column;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class ProcurementResponse {
    private UUID id;
    private String name;
    private List<ProcurementDetailResponse> procurement_detail;
    private Boolean is_approved_manager;
    private Boolean is_approved_finance;
    private Boolean is_rejected_manager;
    private Boolean is_rejected_finance;
    private Boolean is_deleted;
    private String created_at;
    private Date updated_at;
    private String created_by;
    private String updated_by;
}
