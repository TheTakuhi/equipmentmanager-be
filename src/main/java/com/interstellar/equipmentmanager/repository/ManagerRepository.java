package com.interstellar.equipmentmanager.repository;

import com.interstellar.equipmentmanager.model.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, UUID>, JpaSpecificationExecutor<Manager> {
}
