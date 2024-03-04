package com.interstellar.equipmentmanager.model.entity;

import com.interstellar.equipmentmanager.model.enums.AuditActionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
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
    @Enumerated(EnumType.STRING)
    private AuditActionType createdBy;
    @Column(name = "last_modified_by")
    @LastModifiedBy
    @Enumerated(EnumType.STRING)
    private AuditActionType lastModifiedBy;
}
