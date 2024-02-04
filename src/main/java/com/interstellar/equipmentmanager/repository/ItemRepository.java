package com.interstellar.equipmentmanager.repository;

import com.interstellar.equipmentmanager.model.entity.Item;
import com.interstellar.equipmentmanager.model.entity.User;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID>, JpaSpecificationExecutor<Item> {
    Page<Item> findAll(Specification<Item> spec, Pageable pageable);

    @Query("SELECT i FROM Item i WHERE i.owner.id = :ownerId AND i.state != 'DISCARDED'")
    List<Item> findAllByOwnerIdAndNotDiscarded(@NonNull @Param("ownerId") UUID id);


}
