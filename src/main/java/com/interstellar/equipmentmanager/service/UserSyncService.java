package com.interstellar.equipmentmanager.service;

import com.interstellar.equipmentmanager.model.dto.keycloak.user.out.KeycloakUserDTO;
import com.interstellar.equipmentmanager.model.dto.user.out.UserDTO;
import org.springframework.lang.NonNull;

public interface UserSyncService {
    @NonNull
    UserDTO syncUserFromKeycloak(@NonNull KeycloakUserDTO keycloakUser);
}
