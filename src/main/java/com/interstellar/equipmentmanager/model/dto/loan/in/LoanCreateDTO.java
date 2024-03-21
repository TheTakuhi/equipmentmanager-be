package com.interstellar.equipmentmanager.model.dto.loan.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LoanCreateDTO {
    @NotNull(message = "Item id is mandatory")
    private UUID itemId;

    @NotNull(message = "Borrower id is mandatory")
    private UUID borrowerId;

    @NotBlank(message = "Loan date is mandatory")
    private String loanDate;
}