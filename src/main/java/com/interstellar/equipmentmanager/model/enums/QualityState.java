package com.interstellar.equipmentmanager.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum QualityState {

    NEW,
    GOOD,
    SLIGHTLY_USED,
    USED,
    DAMAGED;


    public static List<String> asStringList() {
        return Arrays.stream(QualityState.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
