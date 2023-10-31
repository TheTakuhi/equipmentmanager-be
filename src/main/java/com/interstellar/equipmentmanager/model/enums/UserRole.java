package com.interstellar.equipmentmanager.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRole {
    GUEST(0),
    MANAGER(1<<1),
    ADMIN(1<<2);

    private final int power;
}
