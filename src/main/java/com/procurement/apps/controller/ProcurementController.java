package com.procurement.apps.controller;

import com.procurement.apps.model.ProcurementRequest;
import com.procurement.apps.service.ProcurementService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ProcurementController {
    private final ProcurementService procurementService;
    @Value("${BASE_URL}")
    private String BASE_URL;
    @Value("[procurement-service]")
    private String SERVICE_NAME;

    @GetMapping
    public ResponseEntity hello(){
        return new ResponseEntity("Procurement-Service is Online", HttpStatus.OK);
    }

    @Operation(summary = "Get all procurements")
    @GetMapping("/v1/procurements")
    public ResponseEntity getAllProcurement(){
        var result = procurementService.getAllProcurement();
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @GetMapping("/v1/procurements/{procurement_id}")
    public ResponseEntity getProcurementById(@PathVariable String procurement_id){
        var result = procurementService.getProcurementById(procurement_id);
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @PostMapping("/v1/procurements")
    public ResponseEntity createProcurement(@RequestBody ProcurementRequest request){
        var result = procurementService.createProcurement(request);
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @PatchMapping("/v1/procurements")
    public ResponseEntity updateProcurement(@RequestBody ProcurementRequest request){
        var result = procurementService.updateProcurement(request);
        return ResponseEntity.status(result.getCode()).body(result);
    }
}
