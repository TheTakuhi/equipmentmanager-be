package com.interstellar.equipmentmanager.service;

import com.interstellar.equipmentmanager.model.dto.keycloak.user.out.KeycloakUserDTO;
import com.interstellar.equipmentmanager.model.dto.keycloak.user.in.KeycloakUserEditDTO;
import com.interstellar.equipmentmanager.model.enums.UserRole;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface KeycloakService {
    Set<KeycloakUserDTO> getUsersByRoles(UserRole... userRoles);

    KeycloakUserDTO getUserByLdapId(@NonNull UUID ldapid, boolean fetchRoles);
    void updateUserInKeycloak(@NonNull UUID ldapId, @NonNull KeycloakUserEditDTO editDTO);

    boolean existsUserInKeycloak(UUID ldapId);

    List<UserRole> getUserRolesByLdapId(UUID ldapId);
}
