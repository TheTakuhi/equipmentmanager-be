package com.interstellar.equipmentmanager.model.dto.item.out;

import com.interstellar.equipmentmanager.model.enums.QualityState;
import com.interstellar.equipmentmanager.model.enums.State;
import com.interstellar.equipmentmanager.model.enums.Type;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ItemCroppedDTO {

    private UUID id;

    private String serialCode;

    private String comment;

    private Type type;

    private State state;

    private QualityState qualityState;

    private LocalDate creationDate;

    private UUID ownerId;

    private List<UUID> loansId;

}
