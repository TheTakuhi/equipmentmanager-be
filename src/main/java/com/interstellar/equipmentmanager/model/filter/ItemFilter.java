package com.interstellar.equipmentmanager.model.filter;

import com.interstellar.equipmentmanager.model.enums.QualityState;
import com.interstellar.equipmentmanager.model.enums.State;
import com.interstellar.equipmentmanager.model.enums.Type;
import com.interstellar.equipmentmanager.model.enums.UserRole;
import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemFilter {
    private String serialCode;
    private Type type;
    private State state;
    private QualityState qualityState;
    private Boolean includeDiscarded;
}