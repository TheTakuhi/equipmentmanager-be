package com.interstellar.equipmentmanager.service;

import com.interstellar.equipmentmanager.model.dto.KeycloakUserDTO.KeycloakUserDTO;
import com.interstellar.equipmentmanager.model.dto.UserDTO.UserDTO;
import org.springframework.lang.NonNull;

public interface UserSyncService {
    @NonNull
    UserDTO syncUserFromKeycloak(@NonNull KeycloakUserDTO keycloakUser);
}
