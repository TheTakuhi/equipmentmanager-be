package com.interstellar.equipmentmanager.model.dto.contract.out;

import com.interstellar.equipmentmanager.model.enums.ContractType;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ContractCroppedDTO {
    private UUID id;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double workedHours;
    private Integer hourLimit;
    private ContractType contractType;
    private UUID contractOwnerID;
}
