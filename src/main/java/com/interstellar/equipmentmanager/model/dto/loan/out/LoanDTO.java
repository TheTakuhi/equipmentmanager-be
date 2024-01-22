package com.interstellar.equipmentmanager.model.dto.loan.out;

import com.interstellar.equipmentmanager.model.dto.item.out.ItemCroppedDTO;
import com.interstellar.equipmentmanager.model.dto.user.out.UserCroppedDTO;
import lombok.*;

import java.util.UUID;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LoanDTO {


    private UUID id;
    private ItemCroppedDTO itemCroppedDTO;
    private UserCroppedDTO borrowerCroppedDTO;
    private UserCroppedDTO lenderCroppedDTO;
    private String loanDate;
    private String returnDate;
}
