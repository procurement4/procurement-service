package com.procurement.apps.controller;

import com.procurement.apps.service.ProcurementDetailService;
import com.procurement.apps.service.ProcurementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ProcurementDetailController {
    private final ProcurementDetailService procurementDetailService;
    @Value("${BASE_URL}")
    private String BASE_URL;
    @Value("[procurement-service]")
    private String SERVICE_NAME;

    @GetMapping("/v1/procurement_details/{procurement_id}")
    public ResponseEntity getProcurementDetailByProcurementId(@PathVariable String procurement_id){
        var result = procurementDetailService.getProcurementDetailByProcurementId(procurement_id);
        return ResponseEntity.status(result.getCode()).body(result);
    }
}
