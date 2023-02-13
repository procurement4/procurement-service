package com.procurement.apps.service;

import com.google.gson.Gson;
import com.procurement.apps.entity.Procurement;
import com.procurement.apps.entity.ProcurementDetail;
import com.procurement.apps.model.*;
import com.procurement.apps.repository.ProcurementDetailRepositoryJPA;
import com.procurement.apps.repository.ProcurementRepositoryJPA;
import com.procurement.apps.utils.ResponseAPI;
import com.procurement.apps.utils.ValidationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcurementService {
    private final ProcurementRepositoryJPA procurementRepository;
    private final ProcurementDetailRepositoryJPA procurementDetailRepository;
    private final ResponseAPI responseAPI;
    @Autowired
    private final ModelMapper modelMapper;
    @Value("[procurement-service]")
    private String SERVICE_NAME;
    @Value("${PRODUCT_SERVICE_URL}")
    private String PRODUCT_SERVICE_URL;

    public ResponseAPI getAllProcurement(){
        try {
            log.info(String.format("%s procurementService.getAllProcurement is called", SERVICE_NAME));
            var getAllProcurement = procurementRepository.findAll().stream().filter(x -> x.getIs_deleted().equals(false)).toList();
            var data = getAllProcurement.stream().map(x -> modelMapper.map(x, ProcurementResponse.class)).toList();
            log.info(String.format("%s Result : %s", SERVICE_NAME, new Gson().toJson(data)));
            return responseAPI.OK("Success get data", data);
        }catch (Exception ex){
            var errMsg = String.format("Error Message : %s with Stacktrace : %s",ex.getMessage(),ex.getStackTrace());
            log.error(String.format("%s" , errMsg));
            return responseAPI.INTERNAL_SERVER_ERROR(errMsg,null);
        }
    }

    public ResponseAPI getProcurementById(String procurement_id){
        try {
            var procurementId = UUID.fromString(procurement_id);
            var getProcurementById = procurementRepository.findById(procurementId);
            if (getProcurementById.isEmpty()) return responseAPI.NOT_FOUND("Procurement not found", null);

            var getProcurementDetail = procurementDetailRepository.findByProcurementId(procurementId);

            var detailProcurement = getProcurementDetail.stream().map(x -> modelMapper.map(x, ProcurementDetailResponse.class)).toList();
            for (var item: detailProcurement) {
                try {
                    WebClient.Builder builder = WebClient.builder();
                    ResponseAPI response = builder.build()
                            .get()
                            .uri(PRODUCT_SERVICE_URL + "/v1/products/" + item.getProduct_id())
                            .retrieve()
                            .bodyToMono(ResponseAPI.class)
                            .block();
                    var productDetail = modelMapper.map(response.getData(), ProductResponse.class);
                    item.setProduct_detail(productDetail);
                    log.info(new Gson().toJson(response));
                }catch (Exception ex){
                    log.info(String.format("%s Error : %s" , SERVICE_NAME, ex.getMessage()));
                }
            }

            var data = modelMapper.map(getProcurementById, ProcurementResponse.class);
            data.setProcurement_detail(detailProcurement);
            return responseAPI.OK("Success get data", data);
        }catch (Exception ex){
            var errMsg = String.format("Error Message : %s with Stacktrace : %s",ex.getMessage(),ex.getStackTrace());
            log.error(String.format("%s" , errMsg));
            return responseAPI.INTERNAL_SERVER_ERROR(errMsg,null);
        }
    }

    public ResponseAPI createProcurement(ProcurementRequest request){
        try {
            //Validate request
            var validate = new ValidationRequest(request).validate();
            if (validate.size() > 0){
                log.info(String.format("Validate Error : %s", validate.toString()));
                return responseAPI.BAD_REQUEST(validate.toString(), null);
            }

            var newProcurement = modelMapper.map(request, Procurement.class);
            newProcurement.setCreated_at(new Date());
            newProcurement.setUpdated_at(new Date());
            newProcurement.setCreated_by(request.getUser_id());
            newProcurement.setUpdated_by(request.getUser_id());
            procurementRepository.save(newProcurement);

            for (var procurementDetail : request.getDetail_procurement()) {
                var newProcurementDetail = modelMapper.map(procurementDetail, ProcurementDetail.class);
                newProcurementDetail.setProcurement_id(newProcurement.getId());
                newProcurementDetail.setUser_id(request.getUser_id());
                newProcurementDetail.setSubtotal(procurementDetail.getQuantity() * procurementDetail.getPrice());
                newProcurementDetail.setCreated_at(new Date());
                newProcurementDetail.setUpdated_at(new Date());
                newProcurementDetail.setCreated_by(request.getUser_id());
                newProcurementDetail.setUpdated_by(request.getUser_id());
                procurementDetailRepository.save(newProcurementDetail);
            }

            var data = modelMapper.map(newProcurement, ProcurementResponse.class);
            return responseAPI.OK("Success create new procurement", data);
        }catch (Exception ex){
            var errMsg = String.format("Error Message : %s with Stacktrace : %s",ex.getMessage(),ex.getStackTrace());
            log.error(String.format("%s" , errMsg));
            return responseAPI.INTERNAL_SERVER_ERROR(errMsg,null);
        }
    }

    public ResponseAPI updateProcurement(ProcurementRequest request){
        try {
            //Validate request
            var validate = new ValidationRequest(request).validate();
            if (validate.size() > 0) return responseAPI.BAD_REQUEST(validate.toString(), null);
            var procurementId = UUID.fromString(request.getId());
            var getProcurement = procurementRepository.findById(procurementId);
            if (getProcurement.isEmpty()) return responseAPI.INTERNAL_SERVER_ERROR("Procurement not found", null);
            var getProcurementDetail = procurementDetailRepository.findByProcurementId(procurementId);
            if (getProcurementDetail.isEmpty()) return responseAPI.INTERNAL_SERVER_ERROR("Procurement Detail not found", null);

            // delete existing procurement detail
            procurementDetailRepository.deleteAllById(getProcurementDetail.stream().map(x -> x.getId()).toList());

            var updatedProcurement = modelMapper.map(request, Procurement.class);
            updatedProcurement.setCreated_at(getProcurement.get().getCreated_at());
            updatedProcurement.setUpdated_at(new Date());
            updatedProcurement.setCreated_by(getProcurement.get().getCreated_by());
            updatedProcurement.setUpdated_by(request.getUser_id());
            procurementRepository.save(updatedProcurement);

            for (var procurementDetail : request.getDetail_procurement()) {
                var updatedDetail = modelMapper.map(procurementDetail, ProcurementDetail.class);
                updatedDetail.setProcurement_id(procurementId);
                updatedDetail.setSubtotal(procurementDetail.getQuantity() * procurementDetail.getPrice());
                updatedDetail.setUser_id(request.getUser_id());
                updatedDetail.setCreated_at(getProcurement.get().getCreated_at());
                updatedDetail.setUpdated_at(new Date());
                updatedDetail.setCreated_by(getProcurement.get().getCreated_by());
                updatedDetail.setUpdated_by(request.getUser_id());
                procurementDetailRepository.save(updatedDetail);
            }

            // update stock product if procurement already approved by manager & finance
            if (request.getIs_approved_manager().equals(true) &&
                request.getIs_approved_finance().equals(true) &&
                request.getIs_rejected_manager().equals(false) &&
                request.getIs_rejected_finance().equals(false))
            {
                try {
                    for (var product : request.getDetail_procurement()) {
                        var updateStock = new UpdateStockRequest();
                        updateStock.setId(product.getProduct_id());
                        updateStock.setStock(product.getQuantity());
                        updateStock.setUser_id(request.getUser_id());

                        WebClient.Builder builder = WebClient.builder();
                        ResponseAPI response = builder.build()
                                .patch()
                                .uri(PRODUCT_SERVICE_URL + "/v1/products/update_stock")
                                .body(Mono.just(updateStock), UpdateStockRequest.class)
                                .retrieve()
                                .bodyToMono(ResponseAPI.class)
                                .block();
                        log.info(new Gson().toJson(response));
                    }
                }catch (Exception ex){
                    log.info(String.format("%s Error : %s" , SERVICE_NAME, ex.getMessage()));
                }
            }
            return responseAPI.OK("Success update procurement", null);
        }catch (Exception ex){
            var errMsg = String.format("Error Message : %s with Stacktrace : %s",ex.getMessage(),ex.getStackTrace());
            log.error(String.format("%s" , errMsg));
            return responseAPI.INTERNAL_SERVER_ERROR(errMsg,null);
        }
    }
}
