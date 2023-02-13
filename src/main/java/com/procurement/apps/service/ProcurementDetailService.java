package com.procurement.apps.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.procurement.apps.model.ProcurementDetailResponse;
import com.procurement.apps.model.ProcurementResponse;
import com.procurement.apps.model.ProductResponse;
import com.procurement.apps.repository.ProcurementDetailRepositoryJPA;
import com.procurement.apps.repository.ProcurementRepositoryJPA;
import com.procurement.apps.utils.ResponseAPI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcurementDetailService {
    private final ProcurementDetailRepositoryJPA procurementDetailRepository;
    private final ResponseAPI responseAPI;
    @Autowired
    private final ModelMapper modelMapper;
    @Value("[procurement-service]")
    private String SERVICE_NAME;
    @Autowired
    ObjectMapper om = new ObjectMapper();
    private Object id;

    public ResponseAPI getProcurementDetailByProcurementId(String procurement_id){
        try {
            var id = UUID.fromString(procurement_id);
            var getProcurementDetail = procurementDetailRepository.findByProcurementId(id);
            String url = "https://product-service.procurement-capstone.site/api";
            var data = getProcurementDetail.stream().map(x -> modelMapper.map(x, ProcurementDetailResponse.class)).toList();
            for (var item: data) {
                try {
                    WebClient.Builder builder = WebClient.builder();
                    ResponseAPI response = builder.build()
                            .get()
                            .uri(url + "/v1/products/" + item.getProduct_id())
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
            return responseAPI.OK("Success get data", data);
        }catch (Exception ex){
            var errMsg = String.format("Error Message : %s with Stacktrace : %s",ex.getMessage(),ex.getStackTrace());
            log.error(String.format("%s" , errMsg));
            return responseAPI.INTERNAL_SERVER_ERROR(errMsg,null);
        }
    }
}
