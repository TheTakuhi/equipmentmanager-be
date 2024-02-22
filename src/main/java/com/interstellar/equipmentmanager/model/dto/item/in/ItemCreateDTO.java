package com.interstellar.equipmentmanager.model.dto.item.in;

import com.interstellar.equipmentmanager.model.dto.user.out.UserCroppedDTO;
import com.interstellar.equipmentmanager.model.enums.QualityState;
import com.interstellar.equipmentmanager.model.enums.State;
import com.interstellar.equipmentmanager.model.enums.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ItemCreateDTO {

    @NotBlank(message = "Serial code is mandatory")
    private String serialCode;

    private String comment;

    @NotNull(message = "Type is mandatory")
    private Type type;

    @NotNull(message = "Quality state is mandatory")
    private QualityState qualityState;

    @NotNull(message = "Owner id is mandatory")
    private UUID ownerId;
}