package com.interstellar.equipmentmanager.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Type {
    MONITOR,
    TABLE,
    LAPTOP,
    DOCKING_STATION,
    CHAIR,
    WEBCAM,
    KEYBOARD,
    HEADPHONES,
    OTHER;

    public static List<String> asStringList() {
        return Arrays.stream(Type.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

}
