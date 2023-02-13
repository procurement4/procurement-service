package com.procurement.apps.repository;

import com.procurement.apps.entity.ProcurementDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;
import java.util.List;

public interface ProcurementDetailRepositoryJPA extends JpaRepository<ProcurementDetail, UUID> {

    @Query(value = "select * from procurement_details pd where pd.procurement_id = :procurement_id", nativeQuery = true)
    List<ProcurementDetail> findByProcurementId(@Param("procurement_id") UUID procurement_id);
}
