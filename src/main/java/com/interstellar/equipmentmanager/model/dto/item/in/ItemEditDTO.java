package com.interstellar.equipmentmanager.model.dto.item.in;

import com.interstellar.equipmentmanager.model.enums.QualityState;
import com.interstellar.equipmentmanager.model.enums.State;
import com.interstellar.equipmentmanager.model.enums.Type;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ItemEditDTO {

    private String serialCode;

    private String comment;

    private Type type;

    private QualityState qualityState;

    private State state;

    private UUID ownerId;
}
