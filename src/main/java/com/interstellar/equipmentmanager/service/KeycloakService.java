package com.interstellar.equipmentmanager.service;

import com.interstellar.equipmentmanager.model.dto.keycloak.user.out.KeycloakUserDTO;
import com.interstellar.equipmentmanager.model.dto.keycloak.user.in.KeycloakUserEditDTO;
import org.springframework.lang.NonNull;

import java.util.UUID;

public interface KeycloakService {
    KeycloakUserDTO getUserByLdapId(@NonNull UUID ldapid, boolean fetchRoles);
    void updateUserInKeycloak(@NonNull UUID ldapId, @NonNull KeycloakUserEditDTO editDTO);
}
