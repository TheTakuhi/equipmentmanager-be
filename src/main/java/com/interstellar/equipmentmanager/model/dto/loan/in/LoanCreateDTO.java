package com.interstellar.equipmentmanager.model.dto.loan.in;

import lombok.*;

import java.util.UUID;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LoanCreateDTO {

    private UUID itemId;
    private UUID borrowerId;
    private String loanDate;
}
