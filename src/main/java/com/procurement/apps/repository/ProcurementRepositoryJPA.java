package com.procurement.apps.repository;

import com.procurement.apps.entity.Procurement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProcurementRepositoryJPA extends JpaRepository<Procurement, UUID> {

}
