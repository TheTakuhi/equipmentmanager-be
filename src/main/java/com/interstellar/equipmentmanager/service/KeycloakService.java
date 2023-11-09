package com.interstellar.equipmentmanager.service;

import com.interstellar.equipmentmanager.exception.KeycloakUserNotFoundException;
import com.interstellar.equipmentmanager.model.dto.keycloak.user.out.KeycloakUserDTO;
import com.interstellar.equipmentmanager.model.dto.keycloak.user.in.KeycloakUserEditDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

public interface KeycloakService {
    List<KeycloakUserDTO> findAllUsers(@Nullable String username, @Nullable Pageable pageable);
    KeycloakUserDTO getUserByLdapId(@NonNull UUID ldapid, boolean fetchRoles) throws KeycloakUserNotFoundException;
    KeycloakUserDTO getUserByPersonalNumber(@NonNull String personalNumber, boolean fetchRoles);
    KeycloakUserDTO getUserByEmail(@NonNull String email, boolean fetchRoles);
    void updateUserInKeycloak(@NonNull UUID ldapId, @NonNull KeycloakUserEditDTO editDTO);
}
