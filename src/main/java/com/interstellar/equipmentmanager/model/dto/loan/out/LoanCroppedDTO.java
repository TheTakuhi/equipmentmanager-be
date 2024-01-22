package com.interstellar.equipmentmanager.model.dto.loan.out;

import lombok.*;

import java.util.UUID;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LoanCroppedDTO {

    private UUID id;
    private UUID itemId;
    private UUID userId;
    private String loanDate;
    private String returnDate;
}
