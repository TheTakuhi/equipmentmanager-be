package com.interstellar.equipmentmanager.model.dto.item.in;

import com.interstellar.equipmentmanager.model.enums.QualityState;
import com.interstellar.equipmentmanager.model.enums.Type;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ItemCreateDTO {

    private String serialCode;

    private String comment;

    private Type type;

    private QualityState qualityState;
}
