package com.interstellar.equipmentmanager.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.*;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.Instant;

@AccessType(AccessType.Type.FIELD)
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AuditInfo {
    @Column(name = "created_at")
    @CreatedDate
    private Instant createdAt;
    @Column(name = "last_modified_at")
    @LastModifiedDate
    private Instant lastModifiedAt;
    @Column(name = "created_by")
    @CreatedBy
    private String createdBy;
    @Column(name = "last_modified_by")
    @LastModifiedBy
    private String lastModifiedBy;
}
