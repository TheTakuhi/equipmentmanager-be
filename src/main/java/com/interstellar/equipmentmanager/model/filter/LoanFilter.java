package com.interstellar.equipmentmanager.model.filter;

import com.interstellar.equipmentmanager.model.enums.Type;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanFilter {

    private String borrowerName;
    private String lenderName;
    private Type type;
    private String serialCode;

}
