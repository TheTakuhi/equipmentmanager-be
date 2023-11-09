package com.interstellar.equipmentmanager.model.dto.audit;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@ToString
@NoArgsConstructor
@Getter
@Setter
public class AuditDTO {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Instant createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Instant lastModifiedAt;
    private String createdBy;
    private String lastModifiedBy;
}
